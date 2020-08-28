package task

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/deps"
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/fileutil"
	"github.com/Jecced/rs/src/rs"
	"log"
)

// 任务容器

var (
	// 等待任务缓存
	waitCache map[string]bool
	// 任务队列
	waitList []entity.FileTask
	// 编译中的缓存
	compileCache map[string]*entity.FileTask
	// 是否在全量编译中
	isFullTask bool
	// 编译阈值
	thresholdValue = 10
)

func init() {
	makeInitValue()
}

// 变量初始化
func makeInitValue() {
	// 等待任务缓存
	waitCache = make(map[string]bool)
	// 任务队列
	waitList = make([]entity.FileTask, 0, 10)
	// 编译中的缓存
	compileCache = make(map[string]*entity.FileTask)
}

// 增加一个编译任务
func PushTask(tsPath string) {
	if isFullTask {
		return
	}
	log.Println("新增任务:", tsPath)
	if _, has := waitCache[tsPath]; has {
		return
	}
	waitCache[tsPath] = true
	task := entity.FileTask{Ts: tsPath}
	waitList = append(waitList, task)

	// 检测是否超过阈值
	if len(waitList) > thresholdValue || len(compileCache) > thresholdValue {
		CompileFullTask()
	}
}

// 终止所有任务, 并通知cocos进行全量编译
func CompileFullTask() {
	log.Println("任务超过阈值, 进行cocos全量编译")
	isFullTask = true
	for _, v := range compileCache {
		if nil == v.Cmd {
			continue
		}
		_ = v.Cmd.Process.Kill()
	}
	// 变量初始化
	makeInitValue()

	// 发送刷新命令
	text := rs.Get(config.CocosUrl + "update-db").Send().ReadText()
	log.Println(text)
}

// 全量编译完成
func CompileFullTaskDone() {
	deps.Refresh()
	isFullTask = false
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
		compileCache[task.Ts] = &task
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
