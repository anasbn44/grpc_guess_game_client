package ma.enset;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Guess;
import ma.enset.stubs.GuessGameGrpc;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 1997).usePlaintext().build();
        GuessGameGrpc.GuessGameStub stub = GuessGameGrpc.newStub(managedChannel);
        Scanner s = new Scanner(System.in);
        final boolean[] done = {false};

        System.out.println("Entrez votre nickName");
        String nickName = s.nextLine();

        StreamObserver<Guess.Response> response = new StreamObserver<Guess.Response>() {
            @Override
            public void onNext(Guess.Response response) {
                System.out.println(response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                done[0] = true;
                System.out.println("the end...");
                System.exit(0);
            }
        };

        while (!done[0]) {
            System.out.println("Entrez un nombre : ");
            int guessedNumber = s.nextInt();
            s.nextLine();

            Guess.Request request = Guess.Request.newBuilder()
                    .setNickName(nickName)
                    .setNumberGuessed(guessedNumber)
                    .build();

            stub.guess(request, response);
            System.in.read();

        }
    }
}