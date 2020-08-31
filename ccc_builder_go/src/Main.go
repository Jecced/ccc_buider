package main

import (
	"ccc_builder_go/src/ccsocket"
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/deps"
	"ccc_builder_go/src/proxy"
	"ccc_builder_go/src/task"
	"ccc_builder_go/src/watch"
)

var (
	path string
)

func init() {
	//path = "/Users/ankang/develop/projects/cocos/ccc_builder_test/"
	path = "/Users/ankang/saisheng/slgrpg"
	config.SetProjectPath(path)
}

func main() {
	// 刷新cocos依赖信息
	deps.Refresh()
	// 监听项目路径
	watch.CocosDir()
	// 启动编译任务
	task.RunTaskQuartz()
	// 启动反向代理服务器
	proxy.RunProxy()
	// 启动websocket
	ccsocket.RunWebSocket()

	select {}
}
