import br.ufal.ic.Facade;
import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) {
        EasyAccept.main(new String[] {"br.ufal.ic.Facade", "tests/us1_1.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.Facade", "tests/us1_2.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.Facade", "tests/us2_1.txt"});
        EasyAccept.main(new String[] {"br.ufal.ic.Facade", "tests/us2_2.txt"});
        
    }
}
