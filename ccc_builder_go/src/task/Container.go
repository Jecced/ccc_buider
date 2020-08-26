package task

import (
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/fileutil"
	"log"
)

// 任务容器

var (
	// 等待任务缓存
	waitCache = make(map[string]bool)
	// 任务队列
	waitList = make([]entity.FileTask, 0, 10)
	// 编译中的缓存
	compileCache = make(map[string]bool)
)

// 增减一个编译任务
func PushTask(tsPath string) {
	log.Println("新增任务:", tsPath)
	if _, has := waitCache[tsPath]; has {
		return
	}
	waitCache[tsPath] = true
	task := entity.FileTask{Ts: tsPath}
	waitList = append(waitList, task)
}

// 获取若干个任务
func GetTask(length int) []entity.FileTask {
	if listLen := len(waitList); length > listLen {
		length = listLen
	}

	var result []entity.FileTask

	for index := 0; true; {
		if index >= len(waitList) || len(result) >= length {
			break
		}

		task := waitList[index]
		// 判断任务是否在编译队列, 如果在
		if _, has := compileCache[task.Ts]; has {
			// 索引+1 并 跳过
			index++
			continue
		}

		// 将任务加入结果集
		result = append(result, task)
		// 从list中移除第index位置
		waitList = append(waitList[:index], waitList[index+1:]...)
		// 缓存中移除
		delete(waitCache, task.Ts)
		// 加入编译队列
		compileCache[task.Ts] = true
	}

	return result
}

// 释放
func Release(task *entity.FileTask) {
	//从编译队列中移除
	delete(compileCache, task.Ts)
	if task.TempDir != "" {
		fileutil.ClearDir(task.TempDir)
	}
}
