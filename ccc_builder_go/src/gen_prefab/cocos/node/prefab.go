package node

type Prefab struct {
	Type               string `json:"__type__"`
	Name               string `json:"_name"`
	ObjFlags           int    `json:"_objFlags"`
	Native             int    `json:"_native"`
	Data               *Id    `json:"data"`
	OptimizationPolicy int    `json:"optimizationPolicy"`
	AsyncLoadAssets    bool   `json:"asyncLoadAssets"`
	Readonly           bool   `json:"readonly"`
}

func NewPrefab() *Prefab {
	return &Prefab{
		Type: "cc.Prefab",
		Data: &Id{},
	}
}
