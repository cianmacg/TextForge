package ie.atu.forge.Stemmers;

/*
https://aclanthology.org/www.mt-archive.info/MT-1968-Lovins.pdf
 */


public class Lovin {

    // Conditions stem must pass to remove ending:
    // No restrictions on stem
    private boolean condition_a(char[] input) {
        return true;
    }

    // Minimum stem length == 3
    private boolean condition_b(char[] input) {
        return input.length >= 3;
    }

    // Minimum stem length == 4
    private boolean condition_c(char[] input) {
        return input.length >= 4;
    }

    // Minimum stem length == 5
    private boolean condition_d(char[] input) {
        return input.length >= 5;
    }

    // Do not remove ending after 'e'
    private boolean condition_e(char[] input) {
        return !(input[input.length - 1] == 'e');
    }

    // Minimum stem length == 3 and do not remove ending after 'e'
    private boolean condition_f(char[] input) {
        return input.length >= 3 && !(input[input.length - 1] == 'e');
    }

    // Minimum stem length == 3 and remove ending only after 'f'
    private boolean condition_g(char[] input) {
        return input.length >= 3 && (input[input.length - 1] == 'f');
    }

    // Its difficult to tell what the first letter is from the original paper. Online sources say 't' and 'll'.
    // Remove stem ending only after 't' or 'll'
    private boolean condition_h(char[] input) {
        return (input[input.length - 1] == 't') || (input[input.length - 1] == 'l' && (input[input.length - 2] == 'l'));
    }

    // Do not remove ending after 'o' or 'e'.
    private boolean condition_i(char[] input) {
        return !(input[input.length - 1] == 'o' || input[input.length - 1] == 'e');
    }

    // Do not remove ending after 'a' or 'e'.
    private boolean condition_j(char[] input) {
        return !(input[input.length - 1] == 'a' || input[input.length - 1] == 'e');
    }


    // Minimum stem length == 3 and remove ending only after 'l', 'i', or 'uαe' (where 'α' is any letter).
    private boolean condition_k(char[] input) {
        return input.length >= 3 && ((input[input.length - 3] == 'u' && input[input.length - 1] == 'e') || input[input.length - 1] == 'l' || input[input.length - 1] == 'i');
    }

    // Do not remove ending after 'u', 'x', or 's', unless 's' follows 'o'.
    private boolean condition_l(char[] input) {
        if(input[input.length - 1] == 'u' || input[input.length - 1] == 'x') {
            return false;
        } else if(input[input.length - 1] == 's' && !(input[input.length - 2] == 'o')) {
            return false;
        }

        return true;
    }

    // Do not remove ending after 'a', 'c', 'e', or 'm'.
    private boolean condition_m(char[] input) {
        return !(input[input.length - 1] == 'a' || input[input.length - 1] == 'c' || input[input.length - 1] == 'e' || input[input.length - 1] == 'm');
    }

    // Minimum stem length == 4 after 'sαα', elsewhere minimum stem length == 3. (where 'α' is any letter)
    private boolean condition_n(char[] input) {
        if(input[input.length - 3] == 's') {
            return input.length >= 4;
        } else {
            return input.length >= 3;
        }
    }

    // Remove ending only after 'l' or 'i'.
    private boolean condition_o(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'i';
    }

    // Do not remove ending after 'c'.
    private boolean condition_p(char[] input) {
        return !(input[input.length - 1] == 'c');
    }

    // Minimum stem length == 3 and do not remove ending after 'l' or 'n'.
    private boolean condition_q(char[] input) {
        return input.length >= 3 && !(input[input.length - 1] == 'l' || input[input.length - 1] == 'n');
    }

    // Remove ending only after 'n' or 'r'.
    private boolean condition_r(char[] input) {
        return input[input.length - 1] == 'n' || input[input.length - 1] == 'r';
    }

    // Remove ending only after 'dr' or 't', unless 't' follows 't'.
    private boolean condition_s(char[] input) {
        return (input[input.length - 2] == 'd' && input[input.length - 1] == 'r') || (input[input.length - 1] == 't' && !(input[input.length - 2] == 't'));
    }

    // Remove ending only after 's' or 't', unless 't' follows 'o'.
    private boolean condition_t(char[] input) {
        return input[input.length - 1] == 's' || (input[input.length - 1] == 't' && !(input[input.length - 2] == 'o'));
    }

    // Remove ending only after 'l', 'm', 'n', or 'r'.
    private boolean condition_u(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'm' || input[input.length - 1] == 'n' || input[input.length - 1] == 'r';
    }

    // Remove ending only after 'c'.
    private boolean condition_v(char[] input) {
        return input[input.length - 1] == 'c';
    }

    // Do not remove ending after 's' or 'u'.
    private boolean condition_w(char[] input) {
        return !(input[input.length - 1] == 's' || input[input.length - 1] == 'u');
    }

    // Remove ending only after 'l', 'i', or 'uαe' (where 'α' is any letter).
    private boolean condition_x(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'i' || (input[input.length - 3] == 'u' && input[input.length - 1] == 'e');
    }

    // Remove ending only after 'in'.
    private boolean condition_y(char[] input) {
        return input[input.length - 2] == 'i' && input[input.length - 1] == 'n';
    }

    // Do not remove ending after 'f'.
    private boolean condition_z(char[] input) {
        return !(input[input.length - 1] == 'f');
    }

    // For readability, I wrote this one a little differently.
    // Remove ending only after 'd', 'f', 'ph', 'th', 'l', 'er', 'or', 'es', or 't'.
    private boolean condition_aa(char[] input) {
        if(input[input.length - 1] == 'd') return true;
        else if (input[input.length - 1] == 'f') return true;
        else if (input[input.length - 2] == 'p' && input[input.length - 1] == 'h') return true;
        else if (input[input.length - 2] == 't' && input[input.length - 1] == 'h') return true;
        else if (input[input.length - 1] == 'l') return true;
        else if (input[input.length - 2] == 'e' && input[input.length - 1] == 'r') return true;
        else if (input[input.length - 2] == 'o' && input[input.length - 1] == 'r') return true;
        else if (input[input.length - 2] == 'e' && input[input.length - 1] == 's') return true;
        else if (input[input.length - 1] == 't') return true;
        return false;
    }

    // Again, written differently for readability.
    // Minimum stem length == 3 and do not remove ending after 'met', or 'ryst'.
    private boolean condition_bb(char[] input) {
        if(input.length >= 3) {
            if(!(input[input.length - 1] == 't')) {
                return true;
            }

            if(input[input.length - 3] == 'm' && input[input.length - 2] == 'e') return false;
            else if (input[input.length - 4] == 'r' && input[input.length - 3] == 'y' && input[input.length - 2] == 's') return false;
        }

        return false;
    }

    // Remove ending only after 'l'.
    private boolean condition_cc(char[] input) {
        return input[input.length - 1] == 'l';
    }
}
