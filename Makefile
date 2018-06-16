stop:
	docker-compose -f docker-compose.yml stop -t 0
	docker-compose -f docker-compose.yml rm -fv

build: stop
	docker-compose -f docker-compose.yml build

deps: build
	docker-compose -f docker-compose.yml up -d