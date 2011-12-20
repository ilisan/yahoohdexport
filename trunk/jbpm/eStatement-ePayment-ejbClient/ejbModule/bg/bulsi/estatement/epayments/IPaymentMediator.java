package bg.bulsi.estatement.epayments;

/**
 * @author tzvetan.stefanov@bul-si.bg
 *
 */
public interface IPaymentMediator {
	
	public void createPayment(String docId, int paymentCode);

}
