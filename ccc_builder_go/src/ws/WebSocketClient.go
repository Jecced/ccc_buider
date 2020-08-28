package ws

import (
	"context"
	"fmt"
	"log"
	"nhooyr.io/websocket"
)

// websocket客户端链接信息
type WebSocketClient struct {
	ctx     context.Context
	cancel  context.CancelFunc
	Conn    *websocket.Conn
	IsConn  bool
	IsClose bool
	ReadMsg chan string
}

// 连接信息构造函数
func NewWebSocketClient() *WebSocketClient {
	c := &WebSocketClient{}
	c.ReadMsg = make(chan string, 10)
	ctx, cancelFunc := context.WithCancel(context.Background())
	c.ctx = ctx
	c.cancel = cancelFunc
	return c
}

func (c *WebSocketClient) Ping() {
	err := c.Conn.Ping(c.ctx)
	if err != nil {
		log.Println("ping 失败", err)
		fmt.Println(err.Error())
	}
}

// 连接方法
func (c *WebSocketClient) Dial(url string) {
	var err error
	c.Conn, _, err = websocket.Dial(c.ctx, url, nil)
	if err != nil {
		log.Println("socket链接失败")
		return
	}
	c.IsConn = true
	c.IsClose = false
	log.Println("socket connection ok!")

	go c.Read()
}

// 发送消息
func (c *WebSocketClient) Send(msg string) {
	if !c.IsConn {
		return
	}
	log.Println("client ->:", msg)
	_ = c.Conn.Write(c.ctx, websocket.MessageText, []byte(msg))
}

// 收取消息
func (c *WebSocketClient) Read() {
	log.Println("client 开始监听服务器消息")
	for {
		read, bytes, err := c.Conn.Read(c.ctx)
		if err != nil {
			log.Println("长连接已经断开", err.Error())
			c.IsClose = true
			c.IsConn = false
			break
		}
		if read == websocket.MessageText {
			c.ReadMsg <- string(bytes)
		}

		// 请求已经关闭, 退出连接
		if c.IsClose {
			return
		}
	}
}

// 关闭请求
func (c *WebSocketClient) Close() {
	_ = c.Conn.Close(websocket.StatusNormalClosure, "exit")
	c.IsClose = true
}
