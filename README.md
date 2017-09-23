# clj-aws-sign

A clojure library that implements the [Signature Version 4 Signing Process](http://docs.aws.amazon.com/general/latest/gr/signature-version-4.html). It is inspired by [s3-beam](https://github.com/martinklepsch/s3-beam) and [this blog post](http://sapient-pair.com/blog/2016/03/08/clojure-aws4-auth/). This library can be used to sign requests, for instance by implementing a signing service.

This library passes the [Signature Version 4 Test Suite](http://docs.aws.amazon.com/general/latest/gr/signature-v4-test-suite.html).

## Usage

Simple example:

```clojure
(clj-aws-sign.core/authorize {:method "GET" :uri "/foo/bar" 
                              :query [["name" "asimov"]] :date "20150830T123600Z" 
							  :headers {"host" "somehost"}
							  :service "s3" :region "ca-central-1"
							  :access-key "myaccesskey" :secret-key "mysecretkey"})
```

Output:

```
AWS4-HMAC-SHA256 Credential=myaccesskey/20150830/ca-central-1/s3/aws4_request, SignedHeaders=host, Signature=e771437ef1c615047cffbf99a20bd20c6e4f361b2600e12534b563065e900bc7
```

You can use this string as `Authorization` header.

The following options are necessary:

* `:method` - The method of the request: `GET`, `POST`, `PUT`

* `:uri` - The uri of the request 

* `date` - Date following [ISO 8601 format](http://docs.aws.amazon.com/general/latest/gr/sigv4-date-handling.html) YYYYMMDD'T'HHMMSS'Z', for instance `"20150830T123600Z"`

* `:access-key` - AWS access key

* `:secret-key`- AWS secret key 

* `:service` - AWS service, for instance `"s3"` 

* `:region` - AWS [region](http://docs.aws.amazon.com/general/latest/gr/rande.html), for instance `"eu-west-1"`

The following options are optional:

* `:query` - Query parameters of the request as vector tuples in a vector: 
`[["name" "Walter"] ["surname" "White"]]`

* `:headers` - Headers of the request you want to sign as map: 
`{"Host" "s3-eu-west-1" "x-amz-content-sha256" "UNSIGNED-PAYLOAD"}`, if you provide a `"x-amz-date"` header `:date` will be overriden

* `:payload` - The request body 

## License

Copyright © 2017 Josef Erben

Distributed under the Eclipse Public License, the same as Clojure.
