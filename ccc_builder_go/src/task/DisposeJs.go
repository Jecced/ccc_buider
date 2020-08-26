package task

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/deps"
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/ccutil"
	"ccc_builder_go/src/util/fileutil"
	"fmt"
	"log"
	"strings"
)

const (
	top = "(function() {\"use strict\";var __module = CC_EDITOR ? module : {exports:{}};var __filename = '%s';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {\"use strict\";\ncc._RF.push(module, '%s', '%s', __filename);\n// %s\n\nObject.defineProperty(exports, \"__esModule\", { value: true });\n"
	bot = "\ncc._RF.pop();\n        }\n        if (CC_EDITOR) {\n            __define(__module.exports, __require, __module);\n        }\n        else {\n            cc.registerModuleFunc(__filename, function () {\n                __define(__module.exports, __require, __module);\n            });\n        }\n        })();\n        //# sourceMappingURL=%s.js.map\n        "
)

// 处理js
func jsDispose(task *entity.FileTask) {
	text, err := fileutil.ReadText(task.TempJs)
	if err != nil {
		log.Println("读取编译后js文件异常", err.Error())
		Release(task)
		return
	}
	//fmt.Println(text)

	indexEsModule := strings.Index(text, "exports.__esModule = true;")
	if -1 == indexEsModule {
		fmt.Println("处理js, 没有找到exports.__esModule = true;")
		Release(task)
		return
	}

	indexSourceMappingURL := strings.Index(text, "//# sourceMappingURL=")
	if -1 == indexSourceMappingURL {
		fmt.Println("处理js, 没有找到sourceMappingURL")
		Release(task)
		return
	}

	body := text[indexEsModule+len("exports.__esModule = true;")+1 : indexSourceMappingURL]

	// top信息
	tsPath := strings.ReplaceAll(task.Ts, config.ListenPath, "")
	previewScripts := strings.ReplaceAll(task.Js, config.QuickScripts, config.PreviewScripts)

	// TODO 查错
	//preview-scripts/assets//script/feature/fog_area/script/FogArea.js
	previewScripts = strings.Replace(previewScripts, "//", "/", -1)

	uuid := ccutil.EncodeByScript(task.Uuid)
	name := task.Name

	topScript := fmt.Sprintf(top, previewScripts, uuid, name, tsPath)

	// bot信息
	botScript := fmt.Sprintf(bot, task.Name)

	script := topScript + body + botScript

	fileutil.WriteText(script, task.Js)

	// 更新依赖
	deps.Update(task.Js)

	// 释放资源
	Release(task)
}
