package proxy

import (
	"ccc_builder_go/src/config"
	"ccc_builder_go/src/deps"
	"fmt"
	"github.com/Jecced/rs/src/rs"
	"io"
	"log"
	"net/http"
	"net/http/httputil"
	"net/url"
)

func RunProxy() {
	log.Println("启动反向代理")
	go startUp()
}

func startUp() {
	http.HandleFunc("/", server)
	http.HandleFunc("/settings.js", settings)
	err := http.ListenAndServe(fmt.Sprintf(":%d", config.ProxyWebPort), nil)
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}

func settings(w http.ResponseWriter, r *http.Request) {

	body2 := rs.Get(config.CocosUrl + "settings.js").SetHeadTimeOut(30_000).SetTimeOut(30_000).Send().ReadText()

	w.Header().Set("Content-Type", "text/javascript;charset=utf-8")

	body := body2 + "\nwindow._CCSettings.scripts = "
	body += deps.GetScriptsDeps()
	_, _ = fmt.Fprintf(w, body) // 刷写body到Response流
}

func server(w http.ResponseWriter, r *http.Request) {
	if r.RequestURI == "/favicon.ico" {
		_, _ = io.WriteString(w, "Request path Error")
		return
	}
	remote, err := url.Parse(config.CocosUrl)
	//remote, err := url.Parse("http://" + "127.0.0.1:7456")
	if err != nil {
		panic(err)
	}
	proxy := httputil.NewSingleHostReverseProxy(remote)
	proxy.ServeHTTP(w, r)
}
