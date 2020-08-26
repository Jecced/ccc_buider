package commutil

import (
	"fmt"
	"io/ioutil"
	"math/rand"
	"os"
	"strings"
)

//获取一个路径下的所有路径
func GetAllDir(path string, list []string) []string {
	dir, _ := ioutil.ReadDir(path)
	for _, file := range dir {
		name := file.Name()
		isDir := file.IsDir()
		if !isDir {
			continue
		}
		url := path + string(os.PathSeparator) + name
		list = append(list, url)
		list = GetAllDir(url, list)
	}
	return list
}

// 获取一个路径下的所有ts文件
func GetAllTsFile(path string, list []string) []string {
	dir, _ := ioutil.ReadDir(path)
	for _, file := range dir {
		name := file.Name()
		isDir := file.IsDir()
		if isDir {
			list = GetAllTsFile(path+string(os.PathSeparator)+name, list)
			continue
		}

		url := path + string(os.PathSeparator) + name

		if strings.HasSuffix(name, ".ts") {
			list = append(list, url)
		}

		//list = append(list, url)
		//list = GetAllDir(url, list)
	}
	return list
}

//生成随机字符串
func RandomString(len int) string {
	//buffer := bytes.Buffer{}
	//rand.Seed(time.Now().Unix())
	//temp := 0
	//for i := 0; i < len; i++{
	//    temp = rand.Intn(36)
	//    if temp < 10{
	//        temp += 48
	//    }else {
	//        temp += 55
	//    }
	//    buffer.WriteString(string(temp))
	//}
	//
	//return buffer.String()
	// https://studygolang.com/topics/12072
	randBytes := make([]byte, len/2)
	rand.Read(randBytes)
	return fmt.Sprintf("%x", randBytes)
}
