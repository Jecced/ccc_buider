package watch

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/task"
	"ccc_builder_go/src/util/commutil"
	"log"
	"os"
	"strings"
	"time"
)

var (
	scripts   = make([]string, 0)
	length    = 0
	timeCache = make(map[string]int64)
)

func CocosDir() {
	dir := config.ListenPath
	// 如果监听目录为/结尾, 则去除
	if strings.HasSuffix(dir, "/") {
		dir = dir[:len(dir)-1]
	}
	scripts = commutil.GetAllTsFile(dir, scripts)
	initTs()
	go scanTs()
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
		time.Sleep(time.Duration(1) * time.Second)
		for i, l := 0, length; i < l; i++ {
			fileInfo, err := os.Stat(scripts[i])
			if err != nil {
				log.Println(err.Error())
				continue
			}
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
