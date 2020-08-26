package task

import (
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/fileutil"
	"log"
	"os/exec"
)

// 编译任务
func compileTasks(tasks []entity.FileTask) {
	for i, l := 0, len(tasks); i < l; i++ {
		task := tasks[i]
		go compileTask(&task)
	}
}

// 编译一个任务
func compileTask(task *entity.FileTask) {
	if !task.DecodeDone {
		log.Println("未解析成功的任务, 跳过", task.Ts)
		return
	}

	// 拷贝ts到临时目录
	_, err := fileutil.FileCopy(task.Ts, task.TempTs)
	if err != nil {
		log.Println(err.Error())
		return
	}

	log.Println("开始编译:", task.TempTs)
	// 执行编译命令
	command := exec.Command("tsc", task.TempTs, "--sourcemap", "--inlineSources")
	_ = command.Run()
	log.Println("编译完成:", task.Js)

	// 处理mapping
	mappingDispose(task)

	// 处理js
	jsDispose(task)
}
