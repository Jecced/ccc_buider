package ccutil

import (
	"fmt"
	"testing"
)

func TestDecode(t *testing.T) {
	out := EncodeByAssets("cc2aaf98-281e-43b6-b814-1f38156f4575")
	fmt.Println(out)
	out = EncodeByScript("cc2aaf98-281e-43b6-b814-1f38156f4575")
	fmt.Println(out)

	out = Decode(out)
	fmt.Println(out)

	s := "d8+YANiK1AiYydVZoLrdgx"
	l := Decode(s)
	fmt.Println(l)
	fmt.Println(EncodeByAssets(l))
	fmt.Println(EncodeByScript(l))
	fmt.Println(s)
}
