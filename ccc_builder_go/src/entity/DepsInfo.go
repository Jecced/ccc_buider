package entity

type DepsInfo struct {
	IsNodeModule bool           `json:"isNodeModule"`
	Deps         map[string]int `json:"deps"`
	File         string         `json:"file"`
}

func NewDepsInfo() *DepsInfo {
	d := &DepsInfo{}
	d.Deps = make(map[string]int)
	return d
}
