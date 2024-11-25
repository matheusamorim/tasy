PRIVATE_KEY = private.key
PUBLIC_KEY = public.key

GEN_PRIVATE_KEY = openssl genpkey -algorithm RSA -out $(PRIVATE_KEY) -aes256
GEN_PUBLIC_KEY = openssl rsa -pubout -in $(PRIVATE_KEY) -out $(PUBLIC_KEY)

$(PRIVATE_KEY):
	$(GEN_PRIVATE_KEY)

$(PUBLIC_KEY): $(PRIVATE_KEY)
	$(GEN_PUBLIC_KEY)

key: $(PRIVATE_KEY) $(PUBLIC_KEY)

clean:
	rm -f $(PRIVATE_KEY) $(PUBLIC_KEY)
