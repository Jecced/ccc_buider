package httputil

import (
	"fmt"
	"io/ioutil"
	"net/http"
)

// get请求转字符串
func Get2Str(url string) (content string, err error) {
	get, err := http.Get(url)
	if err != nil {
		return "", err
	}

	all, err := ioutil.ReadAll(get.Body)
	if err != nil {
		return "", err
	}

	if get.StatusCode != 200 {
		_ = fmt.Errorf("url get status code error %d, %s", get.StatusCode, url)
	}

	return string(all), nil
}
