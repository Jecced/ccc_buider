package watch

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/listener"
	"ccc_builder_go/src/util/commutil"
	"fmt"
	"github.com/fsnotify/fsnotify"
	"log"
)

var (
	watch *fsnotify.Watcher
)

// 开始监听目录下的所有目录
func onListener(dir string) {
	list := make([]string, 0)
	// 将所有目录加入到watch中
	for _, src := range commutil.GetAllDir(dir, list) {
		err := watch.Add(src)
		if err != nil {
			log.Fatal(err)
		}
	}
}

func CocosDir() {
	dir := config.ListenPath
	watch, _ = fsnotify.NewWatcher()
	//defer watcher.Close()
	go observer()
	onListener(dir)
	fmt.Println("开始监听:", dir)
}

func observer() {
	for {
		select {
		case event, ok := <-watch.Events:
			if !ok {
				return
			}
			//fmt.Println("event:", event.Name, event.Op, event)
			//fmt.Println(event.Op)
			//fmt.Println(event.Name)
			//if event.Op & fsnotify.Write == fsnotify.Write {
			//    fmt.Println("modified file:", event.Name)
			//}
			listener.FileListener(&event)
		case err, ok := <-watch.Errors:
			if !ok {
				return
			}
			log.Println("error:", err)
		}
	}
}
