package websocket

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/httputil"
	"encoding/json"
	"fmt"
	"strings"
	"time"
)

// websocket 准备工作

func RunWebSocket() {
	go websocketStartUp()
	go common()
}

func common() {
	time.Sleep(time.Duration(5) * time.Second)
	doGet0()
	polling()
	time.Sleep(time.Duration(2) * time.Second)
	client.dail()
	time.Sleep(time.Duration(1) * time.Second)
	polling()
}

func doGet0() {
	now := time.Now().UnixNano() / 1e6

	path := fmt.Sprintf("socket.io/?EIO=3&transport=polling&t=%d-%d", now, index)
	index++
	str, err := httputil.Get2Str(config.CocosUrl + path)

	if err != nil {
		return
	}

	start := strings.Index(str, "{")
	str = str[start:]

	var cocos entity.CocosSocketGet0

	_ = json.Unmarshal([]byte(str), &cocos)

	sid = cocos.Sid

	fmt.Println("Do Get0", &cocos)
}

func polling() {
	now := time.Now().UnixNano() / 1e6
	path := fmt.Sprintf("socket.io/?EIO=3&transport=polling&t=%d-%d&sid=%s", now, index, sid)
	index++
	str, err := httputil.Get2Str(config.CocosUrl + path)
	if err != nil {
		return
	}
	fmt.Println("Do Get1", str)
}
