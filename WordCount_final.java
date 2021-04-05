package java_report;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class Check implements ActionListener {
	JTable table;
	JTextField txt1;
	JLabel label1;
	int allcnt; // jtable에 있는 단어를 제거할 단어 갯수
	HashMap<String, Integer> WordMap; // 생성자로 받아 저장할 해쉬맵
	StringBuffer Ascii = new StringBuffer(); // String이 문자열 조작에 효율이 떨어져 Buffer이용
	String Ascii_new; // 구획문자를 담을 문자열
	String[] ExceptWord = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "one", "after", "a", "i", "the", "s",
			"have", "including", "two", "under", "many", "such", "was", "who", "as", "an", "at", "be", "by", "which",
			"for", "he", "if", "in", "is", "it", "while", "that", "than", "of", "on", "or", "they", "so", "to", "un",
			"up", "this", "we", "has", "his", "been", "from", "according", "its", "those", "between", "only", "would",
			"well", "were", "added", "may", "about", "into", "because", "not", "when", "one", "out", "will", "with",
			"there", "their", "percent", "during", "other", "all", "and", "000", "are", "through", "both", "most",
			"also", "over", "but", "can", "said", "10", "20", "jae", "moon", "jong", "kim", "lee", "park", "talks",
			"more", "take", "some", "three", "high", "year", "make", "same", "top", "next", "won", "no", "u", "t",
			"had", "being", "30", "day", "month", "since", "years", "could", "made", "them", "like", "our" }; // 제외할 단어의
																												// 배열

	Check(JTable table, JTextField txt1, JLabel label1, HashMap<String, Integer> WordMap, int allcnt) {
		this.table = table;
		this.txt1 = txt1;
		this.label1 = label1;
		this.WordMap = WordMap;
		this.allcnt = allcnt;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		HashMap<String, Integer> map2 = new HashMap<String, Integer>(); // 새로운 해쉬맵을 생성한다.(참조를 피하기위해서)
		StringBuilder copy_builder = new StringBuilder(""); // 문자열 조작 효율을 위해서 Builder이용
		String line = "";
		BufferedReader br = null;
		FileReader fr = null;

		for (String str : WordMap.keySet()) // 기존의 것을 복사하여 사용한다 (기존의 해쉬맵에 영향을 주지않기위해서)
			map2.put(str, WordMap.get(str));
		try {
			terminate(); // 기존의 내용을 지워준다.
			fr = new FileReader(txt1.getText() + ".txt"); // 파일 이름을 받는다.
			br = new BufferedReader(fr); // 파일을 한줄씩 읽어들인다.
			label1.setText(null); // 라벨 부분을 지워준다.
			while ((line = br.readLine()) != null) // 한줄씩 읽어들여서 내용이 없을떄까지 반복
			{
				copy_builder.append(line + ""); // 읽은 내용을 copy라는 문자열에 저장
			}
			br.close();
			String copy = copy_builder.toString();
			putAscii(); // 제외할 구획문자를 만듦.
			Ascii_new = Ascii.toString(); // buffer로 만들어진 내용을 String인 Ascii_new에 저장
			getToken(copy, map2); // copy에서 단어를 분류해서 map2에 저장
			Remover(map2); // map2에서 제외할 단어를 제외
			Load(map2); // 모든 처리가 끝난 내용을 테이블에 출력
		} catch (IOException ioe) {
			terminate(); // 기존에 출력된 내용들을 지운다.
			label1.setText("파일이 존재하지않습니다"); // 파일이 없어서 읽지 못한 경우 "파일이 존재하지않습니다"를 라벨에 띄운다.
			return;
		}
	}

	void sort(String[][] array) // 저장된 data들을 내림차순으로 정렬하는것
	{
		String temp1, temp2, temp3, temp4;
		for (int i = 0; i < array[0].length; i++) {
			int max = i;
			for (int j = i + 1; j < array[0].length; j++) {
				if (Integer.parseInt(array[1][j]) > Integer.parseInt(array[1][max])) // string으로 저장된 내용을 Integer로 바꿔서 비교
					max = j;
			}
			temp1 = array[0][i]; // 단어 이름 부분
			temp2 = array[0][max];
			temp3 = array[1][i]; // cnt 부분
			temp4 = array[1][max];
			array[0][i] = temp2; // 레퍼런스 타입은 스왑이 되지않는다 그래서 프리미티브를 교환시킨다.
			array[0][max] = temp1;
			array[1][i] = temp4;
			array[1][max] = temp3;
		}
	}

	void Remover(HashMap<String, Integer> map2) // 단어를 제외하는 메소드
	{
		for (int i = 0; i < ExceptWord.length; i++)
			map2.remove(ExceptWord[i]);
	}

	void terminate() // 테이블에 출력된 내용을 지우는 메소드
	{
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		if (allcnt != 0) // allcnt가 0일 경우 반복문이 돌아가지 않으므로 0을 제외 시킨다. allcnt는 전의 기사 내용의 수가 저장이 되있다
		{
			for (int i = 0; i < allcnt; i++)
				model.removeRow(0);
			allcnt = 0; // allcnt를 초기화 시켜준다 (다음 파일의 갯수를 세기위해)
		}
	}

	void Load(HashMap<String, Integer> map2) // 화면에 띄우는 메소드
	{
		String a[] = new String[2];
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		String array[][] = new String[2][map2.size()]; // 단어의 갯수만큼 배열을 만든다.
		int j = 0;
		for (String str : map2.keySet()) // 해쉬맵에 있는 내용을 2차원 배열로 옮긴다.
		{
			array[0][j] = str;
			array[1][j] = map2.get(str) + "";
			j++;
		}
		sort(array); // 2차원 배열을 정렬
		for (int i = 0; i < array[0].length; i++) // 배열의 내용을 테이블에 적는다.
		{
			a[0] = array[0][i];
			a[1] = array[1][i];
			allcnt++;
			model.addRow(a);
		}
	}

	void getToken(String copy, HashMap<String, Integer> map2) // 단어를 나누는 메소드
	{
		if (copy != null) // 읽어온 내용이 있을 경우
		{
			StringTokenizer stok = new StringTokenizer(copy, Ascii_new); // putAscii메소드로 만든 내용들을 기준으로 분류
			while (stok.hasMoreTokens()) // 내용이 없을때까지 반복
			{
				String word = stok.nextToken();
				word = word.toLowerCase(); // 같은 문자가 중복 저장되는 것을 피하기 위해 소문자로 다 변경
				Integer cnt = map2.get(word); // 해쉬맵에 있는지 확인
				if (cnt == null) { // 없으면 새로 저장
					map2.put(word, 1);
				} else // 있으면 기존의 cnt만 증가
					map2.put(word, ++cnt);
			}
		}
	}

	void putAscii() // 제외할 구획문자를 저장하는 메소드
	{
		for (int i = 33; i <= 47; i++)
			Ascii.append((char) i);
		for (int i = 58; i <= 64; i++)
			Ascii.append((char) i);
		for (int i = 91; i <= 96; i++)
			Ascii.append((char) i);
		for (int i = 123; i < 127; i++)
			Ascii.append((char) i);
		Ascii.append(" “”‘’"); // "" '' 와는 같지만 형태가 다른 것은 따로 추가
	}

}

public class report3_WordCount {

	public static void main(String[] args) {
		HashMap<String, Integer> WordMap = new HashMap<String, Integer>();
		int allcnt = 0;

		JFrame frame = new JFrame("Word Counter");
		frame.setLocation(500, 200);
		frame.setPreferredSize(new Dimension(500, 400));
		Container contentPane = frame.getContentPane();
		String colNames[] = { "Word", "Count" };
		DefaultTableModel model = new DefaultTableModel(colNames, 0);
		JTable table = new JTable(model);
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JButton btn1 = new JButton("Input");
		JTextField txt1 = new JTextField(30);
		JLabel label1 = new JLabel();
		panel1.setLayout(new java.awt.GridLayout(2, 1));
		panel2.add(new JLabel("File Name"));
		panel2.add(txt1);
		panel2.add(btn1);
		panel3.add(label1);
		panel1.add(panel2);
		panel1.add(panel3);
		btn1.addActionListener(new Check(table, txt1, label1, WordMap, allcnt));
		contentPane.add(panel1, BorderLayout.NORTH);
		contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
