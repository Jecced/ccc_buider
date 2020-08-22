package listener

import (
	"ccc_builder_go/src/task"
	"fmt"
	"github.com/fsnotify/fsnotify"
	"strings"
)

// 监听处理
func FileListener(event *fsnotify.Event) {
	if event.Op&fsnotify.Write != fsnotify.Write {
		return
	}
	//fmt.Println("变动======")
	path := event.Name
	if strings.HasSuffix(path, ".ts") {
		fmt.Println("ts变动")
		task.PushTask(path)
	}

	if strings.HasSuffix(path, ".js") {
		fmt.Println("js变动")
	}
}
