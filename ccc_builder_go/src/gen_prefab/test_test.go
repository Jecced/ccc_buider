package gen_prefab

import (
	"ccc_builder_go/src/gen_prefab/cocos/meta"
	"ccc_builder_go/src/gen_prefab/cocos/node"
	"encoding/json"
	"fmt"
	"testing"
)

func TestGenUuid(t *testing.T) {

	path := "/Users/ankang/develop/projects/cocos/ccc_builder_test/assets/Texture/HelloWorld.png"
	sprite, _ := meta.GenSprite(path)
	fmt.Println(sprite)

	bytes, _ := json.Marshal(sprite)

	fmt.Println(string(bytes))

	n := node.NewPrefab()
	fmt.Println(ToJson(n))
}

func ToJson(obj interface{}) string {
	bytes, _ := json.Marshal(obj)

	return string(bytes)
}
