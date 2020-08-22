package deps

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/entity"
	"ccc_builder_go/src/util/httputil"
	"ccc_builder_go/src/util/jsonutil"
	"encoding/json"
	"fmt"
	"strings"
	"time"
)

// 依赖控制器

var (
	// 导出的脚本信息
	scripts = make([]entity.DepsInfo, 0, 10)

	// 索引顺序
	indexCache = make(map[string]int)
)

// 获取依赖信息
func GetScriptsDeps() string {
	text, _ := json.Marshal(scripts)
	return string(text)
}

// 刷新cocos依赖信息
func Refresh() {
	fmt.Println("刷新cocos依赖信息...")
	time.Sleep(time.Duration(1) * time.Second)
	cocosDeps := getByCocosDeps()
	decodeCocosDeps(cocosDeps)
	fmt.Println("cocos依赖信息获取完成.")
}

// 解析cocos依赖信息
func decodeCocosDeps(cocosDeps []entity.DepsInfo) {
	var deps entity.DepsInfo
	for i, l := 0, len(cocosDeps); i < l; i++ {
		deps = cocosDeps[i]
		scripts = append(scripts, deps)
		start := strings.LastIndex(deps.File, "/")
		end := strings.LastIndex(deps.File, ".")
		name := deps.File[start+1 : end]
		//fmt.Println(name)
		indexCache[name] = i
	}
}

// 从cocos获取默认依赖信息
func getByCocosDeps() []entity.DepsInfo {
	str, err := httputil.Get2Str(config.CocosUrl + "settings.js")
	if err != nil {
		fmt.Println("settings获取失败", err.Error())
		return []entity.DepsInfo{}
	}

	start := strings.Index(str, "scripts: ")
	end := strings.Index(str, "rawAssets: ")
	str = str[start:end]
	start = strings.Index(str, "[")
	end = strings.LastIndex(str, ",")
	str = str[start:end]

	str = jsonutil.ParseJsonKey(str)

	var arr []entity.DepsInfo
	err = json.Unmarshal([]byte(str), &arr)
	if err != nil {
		fmt.Println("格式化json失败", err.Error())
		return []entity.DepsInfo{}
	}
	return arr
}
