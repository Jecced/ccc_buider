package websocket

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/entity"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"strings"
	"time"
)

// websocket 准备工作

func RunWebSocket() {
	go websocketStartUp()
	go common()
}

func common() {
	doGet0()
	polling()
	client.dail()
	go polling()
	time.Sleep(time.Duration(1) * time.Second)
	client.sendMsgChan <- "5"
}

func doGet0() {
	now := time.Now().UnixNano() / 1e6

	path := fmt.Sprintf("socket.io/?EIO=3&transport=polling&t=%d-%d", now, index)
	index++
	str := cocosGet(config.CocosUrl + path)

	start := strings.Index(str, "{")
	str = str[start:]

	var cocos entity.CocosSocketGet0

	_ = json.Unmarshal([]byte(str), &cocos)

	sid = cocos.Sid

	log.Println("Do Get0", &cocos)
}

func polling() {
	now := time.Now().UnixNano() / 1e6
	path := fmt.Sprintf("socket.io/?EIO=3&transport=polling&t=%d-%d&sid=%s", now, index, sid)
	index++
	log.Println("Do Get1...", path)
	str := cocosGet(config.CocosUrl + path)
	log.Println("Do Get1", str)
}

func cocosGet(url string) string {
	client := &http.Client{}
	request, err := http.NewRequest("GET", url, nil)

	if err != nil {
		log.Println("get错误0")
		return ""
	}

	request.Header.Add("Cookie", "io="+sid+"; device=6")
	request.Header.Add("Sec-Fetch-Dest", "empty")
	request.Header.Add("Sec-Fetch-Mode", "cors")
	request.Header.Add("Sec-Fetch-Site", "same-origin")
	request.Header.Add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36")

	response, err := client.Do(request)
	if err != nil {
		log.Println("get错误1")
		return ""
	}
	defer response.Body.Close()

	all, err := ioutil.ReadAll(response.Body)
	if err != nil {
		return ""
	}

	if response.StatusCode != 200 {
		_ = fmt.Errorf("url get status code error %d, %s", response.StatusCode, url)
	}

	return string(all)
}
