package task

// 任务处理中心

// 执行一次
func DoOnce() {

	// 全量编译中, 不执行
	if isFullTask {
		return
	}
	// 从缓存容器中拿取3个任务
	tasks := GetTask(3)
	// 完善任务task信息
	fillTaskInfoList(tasks)
	// 编译task任务
	compileTasks(tasks)
}
