package keysmith.client.client;

import java.security.PublicKey;

import keysmith.client.KeysmithClientConfiguration;
import keysmith.client.core.HttpResponseException;
import keysmith.common.crypto.KeyMaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.config.Environment;

public class KeysmithServerClient extends ApiClient {

	private static final Logger log = LoggerFactory
			.getLogger(KeysmithServerClient.class);

	private final String postKeyURL;

	private final String updateKeyURL;

	private final String getKeyURL;

	private final String removeKeyURL;

	private KeyMaster keyMaster;

	public KeysmithServerClient(Environment environment,
			KeysmithClientConfiguration configuration, KeyMaster keyMaster) {
		super(environment, configuration.getJerseyClientConfiguration());
		this.keyMaster = keyMaster;
		String server = configuration.getKeysmithServer();
		postKeyURL = String.format("%s/keysmith/publicKey", server);
		updateKeyURL = String.format("%s/keysmith/publicKey/%%s", server);
		getKeyURL = String.format("%s/keysmith/publicKey/%%s", server);
		removeKeyURL = String.format("%s/keysmith/publicKey/%%s", server);
	}

	public String updatePublicKey(String keyId, PublicKey key) {
		String url = String.format(updateKeyURL, keyId);
		log.info("updatePublicKey : " + url);
		String keyString = keyMaster.encodePublicKey(key);
		log.info("key encoded : " + keyString);
		try {
			utils.post(client, url, String.class, keyString);
		} catch (HttpResponseException e) {
			log.error("error updating public key", e);
		}
		return keyId;
	}

	public String postPublicKey(PublicKey key) {
		String url = postKeyURL;
		log.info("postPublicKey : " + url);
		String keyString = keyMaster.encodePublicKey(key);
		log.info("key encoded : " + keyString);
		try {
			return utils.post(client, url, String.class, keyString);
		} catch (HttpResponseException e) {
			log.error("error posting public key", e);
		}
		return null;
	}

	public PublicKey getPublicKey(String keyId) {
		String url = String.format(getKeyURL, keyId);
		log.info("getPublicKey : " + url);
		try {
			String keyString = utils.get(client, url, String.class);
			PublicKey key = keyMaster.decodePublicKey(keyString);
			return key;
		} catch (HttpResponseException e) {
			log.error("error getting public key", e);
		}
		return null;
	}

	public PublicKey removePublicKey(String keyId, Client client) {
		String url = String.format(removeKeyURL, keyId);
		log.info("removePublicKey : " + url);
		try {
			String keyString = utils.delete(client, url, String.class);
			PublicKey key = keyMaster.decodePublicKey(keyString);
			return key;
		} catch (HttpResponseException e) {
			log.error("error deleting public key", e);
		}
		return null;
	}

}
