package config

import (
	"ccc_builder_go/src/util/fileutil"
	"fmt"
	"os"
	"strings"
)

var (
	CocosPort = 7456

	CocosUrl = fmt.Sprintf("http://localhost:%d/", CocosPort)

	ProjectPath string

	ListenPath = ProjectPath + "/assets/"

	DescPath = ProjectPath + "/temp/quick-scripts/assets/"

	TempPath = ProjectPath + "/temp/temp-build/"

	QuickScripts = ProjectPath + "/temp/quick-scripts/"

	PreviewScripts = "preview-scripts/"

	ProxyWebPort = 8456
)

func SetCocosPort(port int) {
	CocosPort = port
	CocosUrl = fmt.Sprintf("http://localhost:%d/", CocosPort)
}

func SetProjectPath(path string) {
	if strings.HasSuffix(path, "/") || strings.HasSuffix(path, "\\") {
		path = path[:len(path)-1]
	}

	ProjectPath = path
	ListenPath = ProjectPath + "/assets/"
	DescPath = ProjectPath + "/temp/quick-scripts/assets/"
	TempPath = ProjectPath + "/temp/temp-build/"
	QuickScripts = ProjectPath + "/temp/quick-scripts/"

	sep := string(os.PathSeparator)
	ProjectPath = strings.ReplaceAll(ProjectPath, "/", sep)
	ProjectPath = strings.ReplaceAll(ProjectPath, "\\", sep)
	ListenPath = strings.ReplaceAll(ListenPath, "/", sep)
	ListenPath = strings.ReplaceAll(ListenPath, "\\", sep)
	DescPath = strings.ReplaceAll(DescPath, "/", sep)
	DescPath = strings.ReplaceAll(DescPath, "\\", sep)
	TempPath = strings.ReplaceAll(TempPath, "/", sep)
	TempPath = strings.ReplaceAll(TempPath, "\\", sep)
	PreviewScripts = strings.ReplaceAll(PreviewScripts, "/", sep)
	PreviewScripts = strings.ReplaceAll(PreviewScripts, "\\", sep)
	QuickScripts = strings.ReplaceAll(QuickScripts, "/", sep)
	QuickScripts = strings.ReplaceAll(QuickScripts, "\\", sep)

	fileutil.ClearDir(TempPath)
	fileutil.MkdirAll(TempPath)
}

func PrintPath() {
	fmt.Println("===============================")
	fmt.Printf("%11s: %s\n", "ProjectPath", ProjectPath)
	fmt.Printf("%11s: %s\n", "ListenPath", ListenPath)
	fmt.Printf("%11s: %s\n", "DescPath", DescPath)
	fmt.Printf("%11s: %s\n", "TempPath", TempPath)
}
