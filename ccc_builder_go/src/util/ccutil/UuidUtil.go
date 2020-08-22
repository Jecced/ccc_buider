package ccutil

import (
	"bytes"
	"fmt"
	"strings"
)

var (
	base64Values  = [128]int{}
	hexChar       = [...]rune{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}
	aes64KeyChars []string
)

func init() {
	keys := "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
	for i := 0; i < 123; i++ {
		base64Values[i] = 64
	}
	for i := 0; i < 64; i++ {
		base64Values[int(keys[i])] = i
	}
	aes64KeyChars = strings.Split(keys, "")
}

// 长 uuid 转短 uuid, 脚本
func EncodeByScript(uuid string) string {
	return encode(uuid, 5)
}

// 长 uuid 转短 uuid, 资源
func EncodeByAssets(uuid string) string {
	return encode(uuid, 2)
}

// long to short
func encode(uuid string, headSize int) string {
	uuid = strings.ReplaceAll(uuid, "-", "")
	length := len(uuid)
	i := headSize
	head := uuid[:i]
	sub := ""
	chars := []rune(uuid)
	for i < length {
		hexVal1 := toNumber(chars[i+0])
		hexVal2 := toNumber(chars[i+1])
		hexVal3 := toNumber(chars[i+2])
		sub += aes64KeyChars[(hexVal1<<2)|(hexVal2>>2)]
		sub += aes64KeyChars[((hexVal2&3)<<4)|hexVal3]
		i += 3
	}
	return head + sub
}

func Decode(base64 string) string {
	return decode(base64, "")
}

func decode(base64, diver string) string {
	length := len(base64)

	if length < 22 || length > 25 {
		return base64
	}

	chars := []rune(base64)
	uuidTemp := make([]rune, 32)

	j := 2
	max := 22

	if length == 23 {
		j = 5
		max = 23
	}
	for i := 0; i < j; i++ {
		uuidTemp[i] = chars[i]
	}

	for i := j; i < max; i += 2 {
		j = hexUuid(i, j, base64, uuidTemp)
	}

	out := join(uuidTemp)

	return fmt.Sprintf(
		"%s-%s-%s-%s-%s%s",
		substring(out, 0, 8),
		substring(out, 8, 4),
		substring(out, 12, 4),
		substring(out, 16, 4),
		substring(out, 20, len(out)-20),
		diver,
	)
}

// 截取字符串
func substring(str string, start, length int) string {
	return str[start : start+length]
}

// 16进制话uuid
func hexUuid(i, j int, base64 string, uuidTemp []rune) int {
	chars := []rune(base64)
	lhs := base64Values[chars[i]]
	rhs := base64Values[chars[i+1]]
	uuidTemp[j] = hexChar[lhs>>2]
	j++
	uuidTemp[j] = hexChar[((lhs&3)<<2)|rhs>>4]
	j++
	uuidTemp[j] = hexChar[rhs&0xF]
	j++
	return j
}

func join(chars []rune) string {
	sb := bytes.Buffer{}
	for _, v := range chars {
		if v == 0 {
			continue
		}
		sb.WriteString(string(v))
	}
	return sb.String()
}

func toNumber(c rune) int {
	if c >= 97 {
		return int(c - 97 + 10)
	}
	return int(c - 48)
}
