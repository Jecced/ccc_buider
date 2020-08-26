package watch

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/listener"
	"ccc_builder_go/src/task"
	"ccc_builder_go/src/util/commutil"
	"github.com/fsnotify/fsnotify"
	"log"
	"os"
	"time"
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
			log.Fatal("error222", err)
		}
	}
}

var (
	scripts   = make([]string, 0)
	length    = 0
	timeCache = make(map[string]int64)
)

func CocosDir() {
	dir := config.ListenPath
	scripts = commutil.GetAllTsFile(dir, scripts)
	initTs()
	go scanTs()

	//watch, _ = fsnotify.NewWatcher()
	////defer watcher.Close()
	//go observer()
	//onListener(dir)
	//fmt.Println("开始监听:", dir)
}

func initTs() {
	length = len(scripts)
	for i := 0; i < length; i++ {
		fileInfo, _ := os.Stat(scripts[i])
		timeCache[scripts[i]] = fileInfo.ModTime().Unix()
	}
}

func scanTs() {
	for {
		time.Sleep(time.Duration(2) * time.Second)
		for i, l := 0, length; i < l; i++ {
			fileInfo, _ := os.Stat(scripts[i])
			//修改时间
			modTime := fileInfo.ModTime()
			v, has := timeCache[scripts[i]]
			if !has {
				continue
			}
			if v == modTime.Unix() {
				continue
			}
			//推送任务
			task.PushTask(scripts[i])
			timeCache[scripts[i]] = modTime.Unix()
		}
	}
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
