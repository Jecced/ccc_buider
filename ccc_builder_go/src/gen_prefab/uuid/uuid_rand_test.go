package uuid

import (
	"fmt"
	"testing"
)

func TestGenRandUuid(t *testing.T) {
	fmt.Println(GenRandUuid())
}
