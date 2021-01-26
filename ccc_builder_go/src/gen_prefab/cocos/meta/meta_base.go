package meta

import (
	"ccc_builder_go/src/gen_prefab/uuid"
	"github.com/Jecced/go-tools/src/imgutil"
	"path/filepath"
	"strings"
)

type MetaFace interface {
	AddMeta(key string, meta MetaFace)
}

type MetaBase struct {
	Ver      string              `json:"ver"`
	Uuid     string              `json:"uuid"`
	SubMetas map[string]MetaFace `json:"sub_metas"`
}

func (b *MetaBase) AddMeta(key string, meta MetaFace) {
	b.SubMetas[key] = meta
}

func NewMetaBase(ver, uuid string) *MetaBase {
	return &MetaBase{
		Ver:      ver,
		Uuid:     uuid,
		SubMetas: make(map[string]MetaFace),
	}
}

type MetaSprite struct {
	*MetaBase
	Type       string `json:"type"`
	WrapMode   string `json:"wrap_mode"`
	FilterMode string `json:"filter_mode"`
}

func GenSprite(imgPath string) (*MetaSprite, error) {
	id := uuid.GenRandUuid()
	frame, err := GenSpriteFrame(imgPath, id)
	if err != nil {
		return nil, err
	}
	meta := &MetaSprite{
		MetaBase:   NewMetaBase("2.0.0", id),
		Type:       "sprite",
		WrapMode:   "clamp",
		FilterMode: "bilinear",
	}
	_, name := filepath.Split(imgPath)

	index := strings.LastIndex(name, ".")

	if -1 != index {
		name = name[:index]
	}

	meta.AddMeta(name, frame)
	return meta, nil
}

type MetaSpriteFrame struct {
	*MetaBase
	RawTextureUuid string  `json:"rawTextureUuid"`
	TrimType       string  `json:"trimType"`
	TrimThreshold  float64 `json:"trimThreshold"`
	Rotated        bool    `json:"Rotated"`
	OffsetX        float64 `json:"offsetX"`
	OffsetY        float64 `json:"offsetY"`
	TrimX          float64 `json:"trimX"`
	TrimY          float64 `json:"trimY"`
	Width          int     `json:"Width"`
	Height         int     `json:"Height"`
	RawWidth       int     `json:"rawWidth"`
	RawHeight      int     `json:"rawHeight"`
	BorderTop      float64 `json:"borderTop"`
	BorderBottom   float64 `json:"borderBottom"`
	BorderLeft     float64 `json:"borderLeft"`
	BorderRight    float64 `json:"borderRight"`
}

func GenSpriteFrame(imgPath, rawUuid string) (*MetaSpriteFrame, error) {
	image, err := imgutil.LoadImage(imgPath)
	if err != nil {
		return nil, err
	}
	w, h := image.Bounds().Max.X, image.Bounds().Max.Y
	id := uuid.GenRandUuid()
	//_, name := filepath.Split(imgPath)
	return &MetaSpriteFrame{
		MetaBase:       NewMetaBase("1.0.3", id),
		RawTextureUuid: rawUuid,
		TrimType:       "auto",
		TrimThreshold:  1,
		Rotated:        false,
		OffsetX:        0,
		OffsetY:        0,
		TrimX:          0,
		TrimY:          0,
		Width:          w,
		Height:         h,
		RawWidth:       w,
		RawHeight:      h,
		BorderTop:      0,
		BorderBottom:   0,
		BorderLeft:     0,
		BorderRight:    0,
	}, nil
}
