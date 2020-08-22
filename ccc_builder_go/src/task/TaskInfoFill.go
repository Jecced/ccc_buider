package task

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/commutil"
	"ccc_builder_go/src/util/fileutil"
	"encoding/json"
	"fmt"
	"strings"
)

// 任务信息文件处理
func fillTaskInfoList(tasks []entity.FileTask) {
	for i, l := 0, len(tasks); i < l; i++ {
		task := &tasks[i]
		fillTaskInfo(task)
	}
}

// 补充一个任务信息
func fillTaskInfo(task *entity.FileTask) {
	task.Meta = task.Ts + ".meta"
	metaJson, err := fileutil.ReadText(task.Meta)
	if err != nil {
		fmt.Printf("%s, 没有对应的meta信息, 跳过\n", task.Ts)
		fmt.Println(err.Error())
		// TODO 加入新文件编译队列, 手动生成需要的meta文件和信息
		return
	}

	var meta entity.MetaScript
	err = json.Unmarshal([]byte(metaJson), &meta)
	if err != nil {
		task.Meta = ""
		fmt.Println("解析meta文件json结构失败")
		return
	}

	// 设置ts文件uuid信息
	task.Uuid = meta.Uuid
	last := strings.LastIndex(task.Ts, "/")
	if last == -1 {
		last = strings.LastIndex(task.Ts, "\\")
	}
	lastPoint := strings.LastIndex(task.Ts, ".")
	if last == -1 || lastPoint == -1 || lastPoint < last+1 {
		fmt.Println("文件名提取失败")
		return
	}

	// 脚本名称
	task.Name = task.Ts[last+1 : lastPoint]
	task.Suffix = task.Ts[lastPoint:]

	js := strings.ReplaceAll(task.Ts, config.ListenPath, config.DescPath)
	js = js[:len(js)-3] + ".js"

	// 最终输出js脚本位置
	task.Js = js
	task.JsMap = js + ".map"

	// 生成临时文件的目录和位置
	task.TempDir = config.TempPath + commutil.RandomString(16)
	tempFilePosition := task.TempDir + fileutil.FileSep + task.Name
	task.TempJs = tempFilePosition + ".js"
	task.TempJsMap = tempFilePosition + ".js" + ".map"
	task.TempTs = tempFilePosition + task.Suffix

	// 计算相对路径 map文件会需要
	jsLast := strings.LastIndex(task.Js, "/")
	if -1 == jsLast {
		jsLast = strings.LastIndex(task.Js, "\\")
	}
	tsLast := strings.LastIndex(task.Ts, "/")
	if -1 == tsLast {
		tsLast = strings.LastIndex(task.Ts, "\\")
	}
	if -1 == tsLast || -1 == jsLast {
		return
	}

	task.RelativePath = fileutil.GetRelativePath(task.Js[:jsLast], task.Ts[:tsLast])

	task.DecodeDone = true
}
