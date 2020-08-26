package task

import (
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/fileutil"
	"ccc_builder_go/src/util/jsonutil"
	"encoding/json"
	"log"
)

// 处理mapping
func mappingDispose(task *entity.FileTask) {
	jsMapJson, err := fileutil.ReadText(task.TempJsMap)
	if err != nil {
		log.Println(err.Error())
		return
	}

	var tscMap entity.TscJsMap

	err = jsonutil.Parse(jsMapJson, &tscMap)
	if err != nil {
		log.Println(err.Error())
		return
	}

	cocosMap := entity.CocosJsMap{Version: 3}

	cocosMap.Sources = []string{task.Name + task.Suffix}
	cocosMap.Names = []string{}
	cocosMap.File = ""
	cocosMap.SourceRoot = task.RelativePath
	cocosMap.Mappings = mappingSub(&tscMap.Mappings)
	cocosMap.SourcesContent = tscMap.SourcesContent

	cocosMapText, _ := json.Marshal(cocosMap)
	fileutil.WriteText(string(cocosMapText), task.JsMap)
}

// 处理mapping
func mappingSub(mapping *string) string {
	chars := []rune(*mapping)
	k := 5
	e := 0
	for i, l := 0, len(chars); i < l; i++ {
		if i > k {
			e++
		}
		if chars[i] != ';' {
			break
		}
	}
	return string(chars[e:])
}
