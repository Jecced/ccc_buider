package websocket

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/deps"
	"fmt"
	"github.com/gorilla/websocket"
	"strconv"
	"strings"
	"time"
)

// https://www.jianshu.com/p/8b6147d74d21
type websocketClientManager struct {
	conn        *websocket.Conn
	addr        *string
	path        string
	sendMsgChan chan string
	recvMsgChan chan string
	isAlive     bool
	timeout     int
	close       bool
}

// 构造函数
func NewWsClientManager(addrIp string, addrPort int, path string, timeout int) *websocketClientManager {
	addrString := addrIp + ":" + strconv.Itoa(addrPort)
	var sendChan = make(chan string, 10)
	var recvChan = make(chan string, 10)
	var conn *websocket.Conn
	return &websocketClientManager{
		addr:        &addrString,
		path:        path,
		conn:        conn,
		sendMsgChan: sendChan,
		recvMsgChan: recvChan,
		isAlive:     false,
		timeout:     timeout,
	}
}

// 链接服务端
func (wsc *websocketClientManager) dail() {
	var err error
	wsUrl := "ws://" + *wsc.addr + "/socket.io/?EIO=3&transport=websocket&sid=" + sid
	wsc.conn, _, err = websocket.DefaultDialer.Dial(wsUrl, nil)
	if err != nil {
		fmt.Println("链接错误", err)
		go common()
		return
	}
	wsc.isAlive = true
	wsc.close = false
	fmt.Printf("connecting to %s 链接成功\n", wsUrl)

	deps.Refresh()

	wsc.sendMsgChan <- "2probe"
}

// 发送消息
func (wsc *websocketClientManager) sendMsgThread() {
	go func() {
		for {
			msg := <-wsc.sendMsgChan
			fmt.Printf("send: %s\n", msg)
			err := wsc.conn.WriteMessage(websocket.TextMessage, []byte(msg))
			if err != nil {
				fmt.Println("write:", err)
				continue
			}
		}
	}()
}

// 读取消息
func (wsc *websocketClientManager) readMsgThread() {
	go func() {
		for {
			if wsc.conn == nil {
				continue
			}
			if wsc.isAlive == false {
				continue
			}
			_, message, err := wsc.conn.ReadMessage()
			if err != nil {
				fmt.Println("read close:", err)
				wsc.isAlive = false
				client.close = true
				// 出现错误，退出读取，尝试重连
				break
			}
			fmt.Printf("recv: %s\n", message)
			recvMsg(&message)
			// 需要读取数据，不然会阻塞
			wsc.recvMsgChan <- string(message)
		}
	}()
}

// 开启服务并重连
func (wsc *websocketClientManager) start() {
	go func() {
		for {
			if wsc.close {
				wsc.dail()
			}
			if wsc.isAlive == false {
				//wsc.dail()
				//wsc.sendMsgThread()
				//wsc.readMsgThread()
			}
			time.Sleep(time.Second * time.Duration(wsc.timeout))
		}
	}()
}

func websocketStartUp() {
	ping()
	wsc := NewWsClientManager("localhost", config.CocosPort, "/socket.io/?EIO=3&transport=websocket&sid="+sid, 10)
	client = wsc
	wsc.sendMsgThread()
	wsc.readMsgThread()
	wsc.start()
}

var (
	client *websocketClientManager
)

func ping() {
	go func() {
		time.Sleep(time.Duration(25) * time.Second)
		if client.isAlive == false {
			return
		}
		client.sendMsgChan <- "2"
	}()
}

// 收到的消息进行处理
func recvMsg(byte *[]byte) {
	msg := string(*byte)
	//switch msg {
	//case "3probe":
	//    client.sendMsgChan <- "5"
	//}
	if msg == "3probe" {
		client.sendMsgChan <- "5"
		return
	}
	if -1 != strings.Index(msg, "browser:reload") {
		deps.Refresh()
		return
	}
}
