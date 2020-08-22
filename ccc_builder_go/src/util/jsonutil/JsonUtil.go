package jsonutil

import (
	"encoding/json"
	"regexp"
	"strings"
)

func Parse(text string, v interface{}) error {
	return json.Unmarshal([]byte(text), v)
}

func regJsonData(Data []byte) []byte {
	reg := regexp.MustCompile("([_0-9a-zA-Z]\\w*):")
	regStr := reg.ReplaceAllString(string(Data), `"$1":`)
	//fmt.Printf("%v\n", regStr)

	//字符串替换值为http中的内容
	newStr := strings.Replace(regStr, `"http":`, "http:", -1)
	//fmt.Printf("%v\n", newStr)
	return []byte(newStr)
}

func ParseJsonKey(text string) string {
	bytes := []byte(text)
	bytes = regJsonData(bytes)
	return string(bytes)
}
