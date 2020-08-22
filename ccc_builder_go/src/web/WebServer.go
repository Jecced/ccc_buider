package web

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/deps"
	"fmt"
	"log"
	"net/http"
)

// 依赖请求服务
func DepsWebServer() {
	fmt.Println("开启web容器")
	go startUp()
}

func startUp() {
	http.HandleFunc("/deps.js", getDeps)                                    //设置访问的路由
	err := http.ListenAndServe(fmt.Sprintf(":%d", config.DepsWebPort), nil) //设置监听的端口
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}

func getDeps(w http.ResponseWriter, r *http.Request) {
	//r.ParseForm()  //解析参数，默认是不会解析的
	//fmt.Println(r.Form)  //这些信息是输出到服务器端的打印信息
	//fmt.Println("path", r.URL.Path)
	//fmt.Println("scheme", r.URL.Scheme)
	//fmt.Println(r.Form["url_long"])
	//for k, v := range r.Form {
	//    fmt.Println("key:", k)
	//    fmt.Println("val:", strings.Join(v, ""))
	//}

	w.Header().Set("Content-Type", "text/javascript;charset=utf-8")

	body := "window._CCSettings.scripts = "
	body += deps.GetScriptsDeps()
	_, _ = fmt.Fprintf(w, body) // 刷写body到Response流
}
