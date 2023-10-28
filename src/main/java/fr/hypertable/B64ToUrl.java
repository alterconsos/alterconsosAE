package fr.hypertable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import fr.alterconsos.AppConfig;
import fr.hypertable.AppTransaction.StatusPhase;

public class B64ToUrl  extends Operation {

	@Override public String mainLine() {
		return "D";
	}

	@Override public StatusPhase phaseFaible() throws AppException {
		IAuthChecker ac = AppTransaction.tr().authChecker;
		String id = (ac.getAuthType() == 1 ? "C_" : "P_") + ac.getAuthGrp() + "_" + ac.getAuthUsr() + "_";
		String nom = arg().getS("nom", "");
		String mime = arg().getS("mime", null);
		String text = arg().getS("text", "");
		byte[] file = new byte[0];
		if (mime == null) {
			int i = text.indexOf(':');
			int j = text.indexOf(';');
			int k = text.indexOf(',');
			if (i != -1 && j > i && k == j + 7) {
				mime = text.substring(i+1, j);
				String prefix = "data:" + mime + ";base64,";
				file = Base64.getDecoder().decode(text.substring(prefix.length()));
			} else {
				mime = "text/plain";
                file = "Ce texte n'est pas en base 64".getBytes(StandardCharsets.UTF_8);
            }
		} else {
            file = text.getBytes(StandardCharsets.UTF_8);
        }

		String ext = HTServlet.extOfMime(mime);
		Date d = new Date();
		String dh = AppConfig.sdfjhsm.format(d);
		String key = "-" + dh + "." + ext;
        key = id + URLEncoder.encode(nom, StandardCharsets.UTF_8) + key;

        AppTransaction.tr().provider().putDocument(d.getTime(), key, file);
		resultat.mime = mime;
		resultat.bytes = key.getBytes(AppTransaction.utf8);
		return StatusPhase.brut;
	}

	@Override public void phaseForte() throws AppException {}

}
