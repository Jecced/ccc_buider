package deps

import (
	"ccc_builder_go/src/util/fileutil"
	"log"
	"strings"
)

// 更新一个脚本的依赖
func Update(jsPath string) {
	text, err := fileutil.ReadText(jsPath)
	if err != nil {
		log.Println("读取js文件失败", jsPath, err.Error())
		return
	}

	var temp string

	list := make([]string, 0)

	indexStart, indexEnd := -1, -1

	for end, index := -1, strings.Index(text, " = require(\""); index != -1; {
		temp = text[index:]
		end = strings.Index(temp, ")")
		temp = text[index : end+index]
		text = text[end+index:]
		index = strings.Index(text, " = require(\"")

		//list = append(list, temp)
		indexStart = strings.Index(temp, "\"")
		indexEnd = strings.LastIndex(temp, "\"")

		list = append(list, temp[indexStart+1:indexEnd])
	}

	indexStart = strings.LastIndex(jsPath, "/")
	indexEnd = strings.LastIndex(jsPath, ".")

	name := jsPath[indexStart+1 : indexEnd]

	i, has := indexCache[name]
	if !has {
		log.Println("该脚本当前没有依赖缓存")
		return
	}
	// 获取当前依赖信息
	info := &scripts[i]

	info.Deps = make(map[string]int)

	for _, v := range list {
		indexStart = strings.LastIndex(v, "/")

		depsIndex, has2 := indexCache[v[indexStart+1:]]
		if !has2 {
			log.Println("依赖索引没找到", v)
			continue
		}

		info.Deps[v] = depsIndex
	}

	log.Println("脚本依赖刷新完成", name, info.Deps)
}
