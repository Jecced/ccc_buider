package main

import (
	"ccc_builder_go/src/ccsocket"
	"ccc_builder_go/src/ws"
	"fmt"
	"log"
	"testing"
	"time"
)

func Test01(t *testing.T) {
	get0 := ccsocket.DoGet0()
	fmt.Println(get0.Sid)

	url := "ws://127.0.0.1:7456/socket.io/?EIO=3&transport=websocket&sid=" + get0.Sid

	c := ws.NewWebSocketClient()
	go r(c)
	c.Dial(url)

	c.Send("2probe")
	go ping(c)

	select {}
}

func ping(c *ws.WebSocketClient) {
	for {
		time.Sleep(time.Duration(10) * time.Second)
		c.Ping()
		c.Send("2")
	}
}

func r(c *ws.WebSocketClient) {
	for {
		var msg = <-c.ReadMsg
		log.Println("server:", msg)
		if "3probe" == msg {
			c.Send("5")
		}
	}
}
