KEY_DIR = src/main/resources
PRIVATE_KEY = $(KEY_DIR)/private.key
PUBLIC_KEY = $(KEY_DIR)/public.key

key: $(PRIVATE_KEY) $(PUBLIC_KEY)

$(PRIVATE_KEY): $(KEY_DIR)
	@openssl genpkey -algorithm RSA -out $(PRIVATE_KEY) -pkeyopt rsa_keygen_bits:2048

$(PUBLIC_KEY): $(PRIVATE_KEY)
	@openssl rsa -pubout -in $(PRIVATE_KEY) -out $(PUBLIC_KEY)

$(KEY_DIR):
	@mkdir -p $(KEY_DIR)

clean:
	rm -rf $(KEY_DIR)

.PHONY: key clean
