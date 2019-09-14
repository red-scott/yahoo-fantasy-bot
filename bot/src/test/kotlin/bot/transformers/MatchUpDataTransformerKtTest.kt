package bot.transformers

import bot.messaging_services.Message
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

internal class MatchUpDataTransformerKtTest {

    private lateinit var testScheduler: TestScheduler

    private lateinit var testPublishSubject: PublishSubject<Document>

    private lateinit var faabDocument: Document
    private lateinit var nonFaabDocument: Document

    @BeforeEach
    fun setUp() {
        testScheduler = TestScheduler()
        testPublishSubject = PublishSubject.create()

        faabDocument = Jsoup.parse(
            "<matchup>\n" +
                    "\t<week> 16 </week>\n" +
                    "\t<week_start> 2018-12-18 </week_start>\n" +
                    "\t<week_end> 2018-12-24 </week_end>\n" +
                    "\t<status> postevent </status>\n" +
                    "\t<is_playoffs> 1 </is_playoffs>\n" +
                    "\t<is_consolation> 0 </is_consolation>\n" +
                    "\t<is_matchup_recap_available> 1 </is_matchup_recap_available>\n" +
                    "\t<matchup_grades>\n" +
                    "\t\t<matchup_grade>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.1 </team_key>\n" +
                    "\t\t\t<grade> B </grade>\n" +
                    "\t\t</matchup_grade>\n" +
                    "\t\t<matchup_grade>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.3 </team_key>\n" +
                    "\t\t\t<grade> A+ </grade>\n" +
                    "\t\t</matchup_grade>\n" +
                    "\t</matchup_grades>\n" +
                    "\t<is_tied> 0 </is_tied>\n" +
                    "\t<winner_team_key> 380.l.1269264.t.3 </winner_team_key>\n" +
                    "\t<teams count=\"2\">\n" +
                    "\t\t<team>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.1 </team_key>\n" +
                    "\t\t\t<team_id> 1 </team_id>\n" +
                    "\t\t\t<name> TEST TEAM 1 </name>\n" +
                    "\t\t\t<team_logos>\n" +
                    "\t\t\t\t<team_logo>\n" +
                    "\t\t\t\t\t<size> large </size>\n" +
                    "\t\t\t\t</team_logo>\n" +
                    "\t\t\t</team_logos>\n" +
                    "\t\t\t<division_id> 1 </division_id>\n" +
                    "\t\t\t<waiver_priority> 9 </waiver_priority>\n" +
                    "\t\t\t<faab_balance> 17 </faab_balance>\n" +
                    "\t\t\t<number_of_moves> 41 </number_of_moves>\n" +
                    "\t\t\t<number_of_trades> 0 </number_of_trades>\n" +
                    "\t\t\t<roster_adds>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<coverage_value> 17 </coverage_value>\n" +
                    "\t\t\t\t<value> 0 </value>\n" +
                    "\t\t\t</roster_adds>\n" +
                    "\t\t\t<clinched_playoffs> 1 </clinched_playoffs>\n" +
                    "\t\t\t<league_scoring_type> head </league_scoring_type>\n" +
                    "\t\t\t<draft_position> 7 </draft_position>\n" +
                    "\t\t\t<has_draft_grade> 1 </has_draft_grade>\n" +
                    "\t\t\t<draft_grade> B </draft_grade>\n" +
                    "\t\t\t<managers>\n" +
                    "\t\t\t\t<manager>\n" +
                    "\t\t\t\t\t<manager_id> 1 </manager_id>\n" +
                    "\t\t\t\t\t<nickname> Matt F </nickname>\n" +
                    "\t\t\t\t\t<is_commissioner> 1 </is_commissioner>\n" +
                    "\t\t\t\t</manager>\n" +
                    "\t\t\t</managers>\n" +
                    "\t\t\t<win_probability> 0 </win_probability>\n" +
                    "\t\t\t<team_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 129.86 </total>\n" +
                    "\t\t\t</team_points>\n" +
                    "\t\t\t<team_projected_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 138.11 </total>\n" +
                    "\t\t\t</team_projected_points>\n" +
                    "\t\t</team>\n" +
                    "\t\t<team>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.3 </team_key>\n" +
                    "\t\t\t<team_id> 3 </team_id>\n" +
                    "\t\t\t<name> TEST TEAM 2 </name>\n" +
                    "\t\t\t<team_logos>\n" +
                    "\t\t\t\t<team_logo>\n" +
                    "\t\t\t\t\t<size> large </size>\n" +
                    "\t\t\t\t</team_logo>\n" +
                    "\t\t\t</team_logos>\n" +
                    "\t\t\t<division_id> 1 </division_id>\n" +
                    "\t\t\t<waiver_priority> 6 </waiver_priority>\n" +
                    "\t\t\t<faab_balance> 100 </faab_balance>\n" +
                    "\t\t\t<number_of_moves> 9 </number_of_moves>\n" +
                    "\t\t\t<number_of_trades> 0 </number_of_trades>\n" +
                    "\t\t\t<roster_adds>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<coverage_value> 17 </coverage_value>\n" +
                    "\t\t\t\t<value> 0 </value>\n" +
                    "\t\t\t</roster_adds>\n" +
                    "\t\t\t<clinched_playoffs> 1 </clinched_playoffs>\n" +
                    "\t\t\t<league_scoring_type> head </league_scoring_type>\n" +
                    "\t\t\t<draft_position> 8 </draft_position>\n" +
                    "\t\t\t<has_draft_grade> 1 </has_draft_grade>\n" +
                    "\t\t\t<draft_grade> B </draft_grade>\n" +
                    "\t\t\t<managers>\n" +
                    "\t\t\t\t<manager>\n" +
                    "\t\t\t\t\t<manager_id> 3 </manager_id>\n" +
                    "\t\t\t\t\t<nickname> Sean </nickname>\n" +
                    "\t\t\t\t</manager>\n" +
                    "\t\t\t</managers>\n" +
                    "\t\t\t<win_probability> 1 </win_probability>\n" +
                    "\t\t\t<team_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 208.52 </total>\n" +
                    "\t\t\t</team_points>\n" +
                    "\t\t\t<team_projected_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 131.00 </total>\n" +
                    "\t\t\t</team_projected_points>\n" +
                    "\t\t</team>\n" +
                    "\t</teams>\n" +
                    "</matchup>"
        )

        nonFaabDocument = Jsoup.parse(
            "<matchup>\n" +
                    "\t<week> 16 </week>\n" +
                    "\t<week_start> 2018-12-18 </week_start>\n" +
                    "\t<week_end> 2018-12-24 </week_end>\n" +
                    "\t<status> postevent </status>\n" +
                    "\t<is_playoffs> 1 </is_playoffs>\n" +
                    "\t<is_consolation> 0 </is_consolation>\n" +
                    "\t<is_matchup_recap_available> 1 </is_matchup_recap_available>\n" +
                    "\t<matchup_grades>\n" +
                    "\t\t<matchup_grade>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.1 </team_key>\n" +
                    "\t\t\t<grade> B </grade>\n" +
                    "\t\t</matchup_grade>\n" +
                    "\t\t<matchup_grade>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.3 </team_key>\n" +
                    "\t\t\t<grade> A+ </grade>\n" +
                    "\t\t</matchup_grade>\n" +
                    "\t</matchup_grades>\n" +
                    "\t<is_tied> 0 </is_tied>\n" +
                    "\t<winner_team_key> 380.l.1269264.t.3 </winner_team_key>\n" +
                    "\t<teams count=\"2\">\n" +
                    "\t\t<team>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.1 </team_key>\n" +
                    "\t\t\t<team_id> 1 </team_id>\n" +
                    "\t\t\t<name> TEST TEAM 1 </name>\n" +
                    "\t\t\t<team_logos>\n" +
                    "\t\t\t\t<team_logo>\n" +
                    "\t\t\t\t\t<size> large </size>\n" +
                    "\t\t\t\t</team_logo>\n" +
                    "\t\t\t</team_logos>\n" +
                    "\t\t\t<division_id> 1 </division_id>\n" +
                    "\t\t\t<waiver_priority> 9 </waiver_priority>\n" +
                    "\t\t\t<number_of_moves> 41 </number_of_moves>\n" +
                    "\t\t\t<number_of_trades> 0 </number_of_trades>\n" +
                    "\t\t\t<roster_adds>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<coverage_value> 17 </coverage_value>\n" +
                    "\t\t\t\t<value> 0 </value>\n" +
                    "\t\t\t</roster_adds>\n" +
                    "\t\t\t<clinched_playoffs> 1 </clinched_playoffs>\n" +
                    "\t\t\t<league_scoring_type> head </league_scoring_type>\n" +
                    "\t\t\t<draft_position> 7 </draft_position>\n" +
                    "\t\t\t<has_draft_grade> 1 </has_draft_grade>\n" +
                    "\t\t\t<draft_grade> B </draft_grade>\n" +
                    "\t\t\t<managers>\n" +
                    "\t\t\t\t<manager>\n" +
                    "\t\t\t\t\t<manager_id> 1 </manager_id>\n" +
                    "\t\t\t\t\t<nickname> Matt F </nickname>\n" +
                    "\t\t\t\t\t<is_commissioner> 1 </is_commissioner>\n" +
                    "\t\t\t\t</manager>\n" +
                    "\t\t\t</managers>\n" +
                    "\t\t\t<win_probability> 0 </win_probability>\n" +
                    "\t\t\t<team_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 129.86 </total>\n" +
                    "\t\t\t</team_points>\n" +
                    "\t\t\t<team_projected_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 138.11 </total>\n" +
                    "\t\t\t</team_projected_points>\n" +
                    "\t\t</team>\n" +
                    "\t\t<team>\n" +
                    "\t\t\t<team_key> 380.l.1269264.t.3 </team_key>\n" +
                    "\t\t\t<team_id> 3 </team_id>\n" +
                    "\t\t\t<name> TEST TEAM 2 </name>\n" +
                    "\t\t\t<team_logos>\n" +
                    "\t\t\t\t<team_logo>\n" +
                    "\t\t\t\t\t<size> large </size>\n" +
                    "\t\t\t\t</team_logo>\n" +
                    "\t\t\t</team_logos>\n" +
                    "\t\t\t<division_id> 1 </division_id>\n" +
                    "\t\t\t<waiver_priority> 6 </waiver_priority>\n" +
                    "\t\t\t<number_of_moves> 9 </number_of_moves>\n" +
                    "\t\t\t<number_of_trades> 0 </number_of_trades>\n" +
                    "\t\t\t<roster_adds>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<coverage_value> 17 </coverage_value>\n" +
                    "\t\t\t\t<value> 0 </value>\n" +
                    "\t\t\t</roster_adds>\n" +
                    "\t\t\t<clinched_playoffs> 1 </clinched_playoffs>\n" +
                    "\t\t\t<league_scoring_type> head </league_scoring_type>\n" +
                    "\t\t\t<draft_position> 8 </draft_position>\n" +
                    "\t\t\t<has_draft_grade> 1 </has_draft_grade>\n" +
                    "\t\t\t<draft_grade> B </draft_grade>\n" +
                    "\t\t\t<managers>\n" +
                    "\t\t\t\t<manager>\n" +
                    "\t\t\t\t\t<manager_id> 3 </manager_id>\n" +
                    "\t\t\t\t\t<nickname> Sean </nickname>\n" +
                    "\t\t\t\t</manager>\n" +
                    "\t\t\t</managers>\n" +
                    "\t\t\t<win_probability> 1 </win_probability>\n" +
                    "\t\t\t<team_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 208.52 </total>\n" +
                    "\t\t\t</team_points>\n" +
                    "\t\t\t<team_projected_points>\n" +
                    "\t\t\t\t<coverage_type> week </coverage_type>\n" +
                    "\t\t\t\t<week> 16 </week>\n" +
                    "\t\t\t\t<total> 131.00 </total>\n" +
                    "\t\t\t</team_projected_points>\n" +
                    "\t\t</team>\n" +
                    "\t</teams>\n" +
                    "</matchup>"
        )

        testPublishSubject.subscribeOn(testScheduler)
    }

    @AfterEach
    fun tearDown() {
        testPublishSubject.onComplete()
        testScheduler.shutdown()
    }

    @Test
    fun convertToMatchUpObjectFAAB() {
        val testObserver = TestObserver.create<Pair<Team, Team>>()

        testPublishSubject
            .convertToMatchUpObject()
            .subscribe(testObserver)
        testPublishSubject.onNext(faabDocument)
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValue {
            it.first.faabBalance == 17 && it.second.faabBalance == 100
        }
    }

    @Test
    fun convertToMatchUpObjectNonFAAB() {
        val testObserver = TestObserver.create<Pair<Team, Team>>()

        testPublishSubject
            .convertToMatchUpObject()
            .subscribe(testObserver)
        testPublishSubject.onNext(nonFaabDocument)
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValue {
            it.first.faabBalance == null && it.second.faabBalance == null
        }
    }

    @Test
    fun convertToMatchUpMessageFAAB() {
        val testObserver = TestObserver.create<Message>()

        testPublishSubject
            .convertToMatchUpObject()
            .convertToMatchUpMessage()
            .subscribe(testObserver)
        testPublishSubject.onNext(faabDocument)
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValue { it is Message.MatchUp }
        testObserver.assertValue {
            it.message.contains("FAAB") && !it.message.contains("Waiver Priority")
        }
    }

    @Test
    fun convertToMatchUpMessageNonFAAB() {
        val testObserver = TestObserver.create<Message>()

        testPublishSubject
            .convertToMatchUpObject()
            .convertToMatchUpMessage()
            .subscribe(testObserver)
        testPublishSubject.onNext(nonFaabDocument)
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValue { it is Message.MatchUp }
        testObserver.assertValue {
            it.message.contains("Waiver Priority") && !it.message.contains("FAAB")
        }
    }

    @Test
    fun convertToScoreUpdateMessage() {
        val testObserver = TestObserver.create<Message>()

        testPublishSubject
            .convertToMatchUpObject()
            .convertToScoreUpdateMessage()
            .subscribe(testObserver)
        testPublishSubject.onNext(faabDocument)
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValue { it is Message.Score }
        testObserver.assertValue {
            it.message == "TEST TEAM 1 vs. TEST TEAM 2\\n" +
                    "129.86 (138.11) - 208.52 (131.0)"
        }
    }
}