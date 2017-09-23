# clj-aws-sign

A clojure library that implements the [Signature Version 4 Signing Process](http://docs.aws.amazon.com/general/latest/gr/signature-version-4.html). It is inspired by [s3-beam](https://github.com/martinklepsch/s3-beam) and [this blog post](http://sapient-pair.com/blog/2016/03/08/clojure-aws4-auth/). This library can be used to sign requests, for instance by implementing a signing service.

## Usage

Simple example:


```clojure
(clj-aws-sign.core/authorize {:method "PUT" :uri "/foo/bar" 
                              :region "eu-central-1" :service "s3"
							  :access-key "mykey" :secret "mysecret"})
```

The following options are supported:

* `:method` - The method of the request: `GET`, `POST`, `PUT`

* `:uri` - The uri of the request 

* `:query` - Query parameters of the request as vector tuples in a vector: 
`[["name" "Walter"] ["surname" "White"]]`

* `:headers` - Headers of the request you want to sign as map: 
`{"Host" "s3-eu-west-1" "x-amz-content-sha256" "UNSIGNED-PAYLOAD"}`

* `:payload` - The request body 

* `:region` - AWS [region](http://docs.aws.amazon.com/general/latest/gr/rande.html), for instance `"eu-west-1"`

* `:service` - AWS service, for instance `"s3"` 

* `:access-key` - AWS access key

* `:secret-key`- AWS secret key 

## License

Copyright © 2017 Josef Erben

Distributed under the Eclipse Public License, the same as Clojure.
