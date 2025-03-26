import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.InputMismatchException;

public class ContaBancariaTerminal {
    private double saldo;
    private final String nomeCliente;
    private final String numeroConta;
    private final String agencia;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public ContaBancariaTerminal(String nomeCliente, String numeroConta, String agencia, double saldoInicial) {
        this.nomeCliente = nomeCliente;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldo = saldoInicial;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("\n=== BANCO DIGITAL ===");
            System.out.println("=== CADASTRO DE CONTA ===\n");
            
            String nomeCliente = obterDado(scanner, "Nome do Cliente: ", false);
            String agencia = obterDado(scanner, "Agência (formato 000-0): ", true);
            String numeroConta = obterDado(scanner, "Número da Conta (formato 00000-0): ", true);
            double saldoInicial = obterValor(scanner, "Saldo Inicial: R$ ");
            
            ContaBancariaTerminal conta = new ContaBancariaTerminal(nomeCliente, numeroConta, agencia, saldoInicial);
            
            System.out.println("\n=== CONTA CRIADA COM SUCESSO! ===");
            System.out.printf("""
                Olá %s, obrigado por criar uma conta em nosso banco!
                Agência: %s
                Conta: %s
                Saldo inicial: R$ %s
                """, nomeCliente, agencia, numeroConta, df.format(saldoInicial));
            
            conta.exibirMenu(scanner);
        } finally {
            scanner.close();
        }
    }

    private void exibirMenu(Scanner scanner) {
        int opcao;
        
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Consultar Saldo");
            System.out.println("2 - Realizar Depósito");
            System.out.println("3 - Realizar Saque");
            System.out.println("4 - Exibir Dados da Conta");
            System.out.println("0 - Sair");
            
            opcao = obterOpcao(scanner, "Escolha uma opção: ", 0, 4);
            
            switch (opcao) {
                case 1 -> consultarSaldo();
                case 2 -> {
                    double valor = obterValor(scanner, "Valor do depósito: R$ ");
                    depositar(valor);
                }
                case 3 -> {
                    double valor = obterValor(scanner, "Valor do saque: R$ ");
                    sacar(valor);
                }
                case 4 -> exibirDadosConta();
                case 0 -> System.out.println("\nObrigado por usar nossos serviços. Até logo!");
            }
        } while (opcao != 0);
    }

    private void consultarSaldo() {
        System.out.println("\n=== SALDO ATUAL ===");
        System.out.println("Saldo disponível: R$ " + df.format(saldo));
    }

    private void depositar(double valor) {
        if (valor <= 0) {
            System.out.println("\nErro: Valor de depósito deve ser positivo!");
            return;
        }
        
        saldo += valor;
        System.out.printf("\nDepósito de R$ %s realizado com sucesso!\n", df.format(valor));
        consultarSaldo();
    }

    private void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("\nErro: Valor de saque deve ser positivo!");
            return;
        }
        
        if (valor > saldo) {
            System.out.println("\nErro: Saldo insuficiente para esta operação!");
            consultarSaldo();
            return;
        }
        
        saldo -= valor;
        System.out.printf("\nSaque de R$ %s realizado com sucesso!\n", df.format(valor));
        consultarSaldo();
    }

    private void exibirDadosConta() {
        System.out.println("\n=== DADOS DA CONTA ===");
        System.out.printf("""
            Titular: %s
            Agência: %s
            Conta: %s
            Saldo: R$ %s
            """, nomeCliente, agencia, numeroConta, df.format(saldo));
    }

    // Métodos auxiliares para tratamento de entrada
    private static String obterDado(Scanner scanner, String mensagem, boolean formatar) {
        while (true) {
            System.out.print(mensagem);
            String dado = scanner.nextLine().trim();
            
            if (dado.isEmpty()) {
                System.out.println("Erro: Este campo não pode ser vazio!");
                continue;
            }
            
            if (formatar) {
                dado = formatarNumero(dado);
            }
            
            return dado;
        }
    }

    private static double obterValor(Scanner scanner, String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                double valor = scanner.nextDouble();
                scanner.nextLine(); // Limpar buffer
                
                if (valor < 0) {
                    System.out.println("Erro: O valor não pode ser negativo!");
                    continue;
                }
                
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite um valor numérico válido!");
                scanner.nextLine(); // Limpar buffer
            }
        }
    }

    private static int obterOpcao(Scanner scanner, String mensagem, int min, int max) {
        while (true) {
            try {
                System.out.print(mensagem);
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer
                
                if (opcao < min || opcao > max) {
                    System.out.printf("Erro: Digite um número entre %d e %d!\n", min, max);
                    continue;
                }
                
                return opcao;
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite um número válido!");
                scanner.nextLine(); // Limpar buffer
            }
        }
    }

    private static String formatarNumero(String numero) {
        // Remove todos os caracteres não numéricos
        String apenasDigitos = numero.replaceAll("[^0-9]", "");
        
        // Formata agência (000-0)
        if (apenasDigitos.length() == 4) {
            return apenasDigitos.substring(0, 3) + "-" + apenasDigitos.substring(3);
        }
        
        // Formata conta (00000-0)
        if (apenasDigitos.length() == 6) {
            return apenasDigitos.substring(0, 5) + "-" + apenasDigitos.substring(5);
        }
        
        return numero; // Retorna original se não for possível formatar
    }
}