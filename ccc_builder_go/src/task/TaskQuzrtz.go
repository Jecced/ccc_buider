package task

import "time"

func RunTaskQuartz() {
	go taskQuartz()
}

// 定时任务
func taskQuartz() {
	for {
		time.Sleep(time.Duration(2) * time.Second)
		DoOnce()
	}
}
