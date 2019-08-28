package com.codecool.zsuzsi.puzzlesbackend;

import com.codecool.zsuzsi.puzzlesbackend.model.*;
import com.codecool.zsuzsi.puzzlesbackend.repository.CommentRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.MemberRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.PuzzleRepository;
import com.codecool.zsuzsi.puzzlesbackend.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class PuzzlesBackendApplication {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PuzzleRepository puzzleRepository;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private CommentRepository commentRepository;

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

            Member user1 = Member.builder()
                    .username("Anna")
                    .password(password)
                    .email("anna@anna.hu")
                    .roles(Set.of("USER"))
                    .score(0)
                    .build();
            memberRepository.save(user1);

            Member user2 = Member.builder()
                    .username("Péter")
                    .password(password)
                    .email("peter@peter.hu")
                    .roles(Set.of("USER"))
                    .score(0)
                    .build();
            memberRepository.save(user2);

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

            Puzzle puzzle6 = Puzzle.builder()
                    .category(Category.RIDDLE)
                    .title("Találós kérdés")
                    .instruction("Válaszolj egy szóval!")
                    .puzzleItem("Aki készíti, annak nem kell. Aki megveszi, az nem használja. Aki használja, nem tud róla. Mi az?")
                    .answer("koporsó")
                    .level(Level.EASY)
                    .member(user1)
                    .build();

            Puzzle puzzle7 = Puzzle.builder()
                    .category(Category.MATH_PUZZLE)
                    .title("Zoknik")
                    .instruction("Válaszolj egy számmal!")
                    .puzzleItem("A fiókban van 14 barna zoknid, 14 kék zoknid és 14 fekete zoknid. Hány zoknit kell kivenned (csukott szemmel), hogy biztosan legyen köztük két azonos színű?")
                    .answer("4")
                    .level(Level.EASY)
                    .member(user2)
                    .build();

            Puzzle puzzle8 = Puzzle.builder()
                    .category(Category.WORD_PUZZLE)
                    .title("Szótagoló")
                    .instruction("Milyen szótag egészíti ki mindkét szót?")
                    .puzzleItem("sár- ? -dó")
                    .answer("mos")
                    .level(Level.EASY)
                    .member(user1)
                    .build();

            Puzzle puzzle9 = Puzzle.builder()
                    .category(Category.CIPHER)
                    .title("Titkos közmondás")
                    .instruction("Fejtsd meg a titkosírással írt közmondást! Minde betű az abc egy másik betűjének felel meg, véletlenszerűen. Egy kis segítség: e = 'h', a = 'l', t = 'ű', l = 'k', n = 'd'")
                    .puzzleItem("lqx épsdlq uhzéhű ps, élml hsxq öhkh.")
                    .answer("Aki másnak vermet ás, amga esik bele")
                    .level(Level.MEDIUM)
                    .member(user2)
                    .build();

            Puzzle puzzle10 = Puzzle.builder()
                    .category(Category.PICTURE_PUZZLE)
                    .title("Négyzetek")
                    .instruction("Hány négyzet van a képen?")
                    .puzzleItem("squares40.png")
                    .answer("40")
                    .level(Level.MEDIUM)
                    .member(user1)
                    .build();

            puzzleRepository.saveAll(Arrays.asList(puzzle1, puzzle2, puzzle3, puzzle4, puzzle5, puzzle6, puzzle7, puzzle8, puzzle9, puzzle10));

            Solution solution1 = Solution.builder()
                    .member(user1)
                    .puzzle(puzzle1)
                    .rating(4)
                    .seconds(21)
                    .submissionTime(LocalDateTime.now())
                    .build();

            Solution solution2 = Solution.builder()
                    .member(user2)
                    .puzzle(puzzle1)
                    .rating(5)
                    .seconds(35)
                    .submissionTime(LocalDateTime.now())
                    .build();

            Solution solution3 = Solution.builder()
                    .member(user1)
                    .puzzle(puzzle2)
                    .rating(5)
                    .seconds(62)
                    .submissionTime(LocalDateTime.now())
                    .build();

            Solution solution4 = Solution.builder()
                    .member(user2)
                    .puzzle(puzzle2)
                    .rating(5)
                    .seconds(71)
                    .submissionTime(LocalDateTime.now())
                    .build();

            solutionRepository.saveAll(Arrays.asList(solution1, solution2, solution3, solution4));

            Comment comment1 = Comment.builder()
                    .member(user1)
                    .puzzle(puzzle1)
                    .message("Ez nagyon könnyű!")
                    .submissionTime(LocalDateTime.now())
                    .build();

            Comment comment2 = Comment.builder()
                    .member(user2)
                    .puzzle(puzzle1)
                    .message("Szerintem becsapós.")
                    .submissionTime(LocalDateTime.now())
                    .build();

            Comment comment3 = Comment.builder()
                    .member(user1)
                    .puzzle(puzzle1)
                    .message("Dehogy!")
                    .submissionTime(LocalDateTime.now())
                    .build();

            commentRepository.saveAll(Arrays.asList(comment1, comment2, comment3));
        };
    }

}
