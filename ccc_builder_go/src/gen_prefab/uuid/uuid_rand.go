package uuid

import uuid "github.com/satori/go.uuid"

func GenRandUuid() string {
	return uuid.NewV4().String()
}
