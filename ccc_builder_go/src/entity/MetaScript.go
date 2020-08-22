package entity

type MetaScript struct {
	Ver                string                `json:"ver"`
	Uuid               string                `json:"uuid"`
	IsPlugin           bool                  `json:"isPlugin"`
	LoadPluginInWeb    bool                  `json:"loadPluginInWeb"`
	LoadPluginInNative bool                  `json:"loadPluginInNative"`
	LoadPluginInEditor bool                  `json:"loadPluginInEditor"`
	SubMetas           map[string]MetaScript `json:"subMetas"`
}

func NewMetaScript() *MetaScript {
	meta := MetaScript{}
	meta.Ver = "1.0.5"
	meta.LoadPluginInWeb = true
	meta.SubMetas = make(map[string]MetaScript)
	return &meta
}
