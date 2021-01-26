package test

import (
	"fmt"
	"github.com/Jecced/rs/src/rs"
	"net/url"
	"strconv"
	"strings"
	"testing"
)

// xid := triggered_experiment_ids:[45662847]
// tkk:'444516.1633104591'
// https://blog.csdn.net/life169/article/details/52153929
// https://www.cnblogs.com/by-dream/p/6554340.html
func Test01(t *testing.T) {

}

// 谷歌翻译
func GoogleTranslate(text string) string {
	uri := "https://translate.google.cn/"

	resp := rs.Get(uri).Send().ReadText()
	tkk := getSubText(resp, "tkk:'", "'")
	xid := getSubText(resp, "triggered_experiment_ids:[", "]")

	translateUri := "https://translate.google.cn/translate_a/single?client=webapp&sl=zh-CN&tl=en&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=sos&dt=ss&dt=t&ssel=6&tsel=3&xid=%s&kc=0&tk=%s&q=%s"
	//text := "今天天气很不错"

	tks := tk(text, tkk)

	resp = rs.Get(fmt.Sprintf(translateUri, xid, tks, url.QueryEscape(text))).Send().ReadText()

	out := getSubText(resp, "[[[\"", "\"")
	//fmt.Println(i, out)
	return out
}

// 获取截取文本
func getSubText(str, prefix, suffix string) string {
	start := strings.Index(str, prefix)
	if -1 == start {
		return ""
	}
	start += len(prefix)
	end := strings.Index(str[start:], suffix)
	if -1 == end {
		return ""
	}
	return str[start : end+start]
}

func b(a int32, b string) int32 {
	rb := []rune(b)
	for d := 0; d < len(b)-2; d += 3 {
		c := rb[d+2]
		if 'a' <= c {
			c = c - 87
		}
		if c >= 48 {
			o, _ := strconv.ParseInt(string(c), 10, 32)
			c = rune(o)
		}
		if '+' == rb[d+1] {
			// 补码右移, a >>> c
			c = int32(uint32(a) >> c)
		} else {
			c = a << c
		}
		if '+' == rb[d] {
			a = a + c
		} else {
			a = a ^ c
		}

	}
	return a
}

func tk(a, TTK string) string {
	e := strings.Split(TTK, ".")
	h, _ := strconv.ParseInt(e[0], 10, 32)
	x, _ := strconv.ParseInt(e[1], 10, 64)
	g := make([]int32, 0, 0)
	ra := []rune(a)

	for f, l := 0, len(ra); f < l; f++ {
		c := ra[f]
		if 128 > c {
			g = append(g, c)
			continue
		}
		if 2048 > c {
			g = append(g, c>>6|192)
			continue
		}
		if 55296 == (c&64512) && f+1 < l && 56320 == (ra[f+1]&64512) {
			f++
			c = 65536 + ((c & 1023) << 10) + (ra[f] & 1023)
			g = append(g, c>>18|240, c>>12&63|128)
			continue
		}
		g = append(g, c>>12|224, c>>6&63|128, c&63|128)
	}
	aa := int32(h)
	for d, l := 0, len(g); d < l; d++ {
		aa += g[d]
		aa = b(aa, "+-a^+6")
	}
	aa = b(aa, "+-3^+b+-f")
	bb := int64(aa) ^ x
	if bb < 0 {
		bb = int64(uint32(bb))
	}
	if 0 > bb {
		bb = (bb & 2147483647) + 2147483648
	}
	bb %= 1e6
	return strconv.FormatInt(bb, 10) + "." + strconv.FormatInt(bb^h, 10)
}
