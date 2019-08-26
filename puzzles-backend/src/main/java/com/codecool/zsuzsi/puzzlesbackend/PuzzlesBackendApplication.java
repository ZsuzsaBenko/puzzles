package com.codecool.zsuzsi.puzzlesbackend;

import com.codecool.zsuzsi.puzzlesbackend.model.Category;
import com.codecool.zsuzsi.puzzlesbackend.model.Level;
import com.codecool.zsuzsi.puzzlesbackend.model.Member;
import com.codecool.zsuzsi.puzzlesbackend.model.Puzzle;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class PuzzlesBackendApplication {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PuzzleRepository puzzleRepository;


    public static void main(String[] args) {
        SpringApplication.run(PuzzlesBackendApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init() {

        String password = System.getenv("ADMIN_PASSWORD");

        return args -> {

            Member admin = Member.builder()
                    .username("admin")
                    .password(password)
                    .email("admin@admin.hu")
                    .roles(Set.of("ADMIN", "USER"))
                    .score(0)
                    .build();
            memberRepository.save(admin);


            Puzzle puzzle1 = Puzzle.builder()
                    .category(Category.RIDDLE)
                    .title("Nővérek")
                    .instruction("Válaszolj egy szóval!")
                    .puzzleItem("Öt nővér van a szobában: Anna könyvet olvas, Marcsi főz, Kati sakkozik, Laura vasal. Mit csinál az ötödik testvér?")
                    .answer("sakkozik")
                    .level(Level.EASY)
                    .member(admin)
                    .build();

            Puzzle puzzle2 = Puzzle.builder()
                    .category(Category.MATH_PUZZLE)
                    .title("Béka")
                    .instruction("Válaszolj egy számmal!")
                    .puzzleItem("Egy béka beleesett egy 12 méter mély kútba. Fel tudott ugrani 3 métert, de mindegyik ugrásnál vissza is esett 2 métert. Hányat kellett ugrania, hogy kijusson a kútból?")
                    .answer("10")
                    .level(Level.EASY)
                    .member(admin)
                    .build();

            Puzzle puzzle3 = Puzzle.builder()
                    .category(Category.WORD_PUZZLE)
                    .title("Összetett szavak")
                    .instruction("Melyik szó alkothat szóösszetételt a következő utótagokkal?")
                    .puzzleItem("-vizsgáló, -klub, -szekrény")
                    .answer("könyv")
                    .level(Level.EASY)
                    .member(admin)
                    .build();

            Puzzle puzzle4 = Puzzle.builder()
                    .category(Category.CIPHER)
                    .title("Nyelvtörő")
                    .instruction("Ezt a nyelvtörőt titkosírással írták le: az abc minden betűje \"arréb lett tolva\". Meg tudod fejteni?")
                    .puzzleItem("wégé wfwwéjq qjm wégé wfwwéjq fé wső wégé wfwwéjq.")
                    .answer("Száz sasszem meg száz sasszem az sok száz sasszem.")
                    .level(Level.MEDIUM)
                    .member(admin)
                    .build();

            Puzzle puzzle5 = Puzzle.builder()
                    .category(Category.PICTURE_PUZZLE)
                    .title("Alapműveletek")
                    .instruction("Milyen számok kerülnek a négyzetekbe? A megoldást ilyen formátumban add meg: bal felső szám, jobb felső szám, bal alsó szám, jobb alsó szám - szóközök nélkül, pl. 1,2,3,4!")
                    .puzzleItem("math-3.5.jpeg")
                    .answer("3.5,4.5,9.5,3.5")
                    .level(Level.MEDIUM)
                    .member(admin)
                    .build();

            puzzleRepository.saveAll(Arrays.asList(puzzle1, puzzle2, puzzle3, puzzle4, puzzle5));
        };
    }

}
