package ccsocket

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/deps"
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/ws"
	"encoding/json"
	"fmt"
	"github.com/Jecced/rs/src/rs"
	"log"
	"strconv"
	"strings"
	"time"
)

// websocket 准备工作

var cocosInfo entity.CocosSocketGet0

func RunWebSocket() {
	get0 := DoGet0()

	url := "ws://localhost:" +
		strconv.Itoa(config.CocosPort) +
		"/socket.io/?EIO=3&transport=websocket&sid=" +
		get0.Sid

	c := ws.NewWebSocketClient()
	// 处理服务器返回信息
	go recv(c)
	// 进行长连接
	c.Dial(url)

	c.Send("2probe")
	// 执行ping操作
	go ping(c)
}

func ping(c *ws.WebSocketClient) {
	for {
		time.Sleep(time.Duration(cocosInfo.PingInterval) * time.Millisecond)
		c.Ping()
		c.Send("2")
	}
}

func recv(c *ws.WebSocketClient) {
	for {
		dispose(<-c.ReadMsg, c)
	}
}

func dispose(msg string, c *ws.WebSocketClient) {
	log.Println("server <-:", msg)
	if "3probe" == msg {
		c.Send("5")
		return
	}
	if -1 != strings.Index(msg, "reload") {
		go deps.Refresh()
		return
	}
}

func DoGet0() *entity.CocosSocketGet0 {
	now := time.Now().UnixNano() / 1e6

	path := fmt.Sprintf("socket.io/?EIO=3&transport=polling&t=%d-%d", now, index)
	index++
	str := rs.Get(config.CocosUrl + path).Send().ReadText()

	start := strings.Index(str, "{")
	str = str[start:]

	//var cocos entity.CocosSocketGet0

	_ = json.Unmarshal([]byte(str), &cocosInfo)

	sid = cocosInfo.Sid

	log.Println("Do Get0", &cocosInfo)

	return &cocosInfo
}

func Polling() {
	now := time.Now().UnixNano() / 1e6
	path := fmt.Sprintf("socket.io/?EIO=3&transport=polling&t=%d-%d&sid=%s", now, index, sid)
	index++
	log.Println("Polling...", path)
	str := rs.Get(config.CocosUrl + path).Send().ReadText()
	log.Println("Polling", str)
}
