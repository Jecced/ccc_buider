package entity

type FileTask struct {
	// 等待编译的ts文件
	Ts string
	// 对应Meta文件位置
	Meta string
	// 临时编译的js文件目录
	TempJs string
	// 临时编译的js对应map文件位置
	TempJsMap string
	// 临时的Ts文件位置
	TempTs string
	// 临时目录
	TempDir string
	// 编译后的js文件
	Js string
	// js.map文件
	JsMap string
	// 脚本 的uuid
	Uuid string
	// 脚本的相对路径
	RelativePath string
	// 脚本名称
	Name string
	// 脚本后缀
	Suffix string
	// decode是否完成
	DecodeDone bool
	// 是否编译完成
	CompileDone bool
}
