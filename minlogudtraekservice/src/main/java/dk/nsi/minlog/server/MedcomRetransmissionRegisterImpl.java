package dk.nsi.minlog.server;

import com.trifork.dgws.MedcomRetransmission;
import com.trifork.dgws.MedcomRetransmissionRegister;
import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

@Repository
public class MedcomRetransmissionRegisterImpl implements MedcomRetransmissionRegister {
    private static final Logger logger = Logger.getLogger(MedcomRetransmissionRegisterImpl.class);

    @Inject
    Marshaller marshaller;

    @Inject
    Unmarshaller unmarshaller;


    @Override
    public MedcomRetransmission getReplay(String messageID) {
        return null;
    }

    @Override
    public void createReplay(String messageID, Object responseMessage) {
    }
}
