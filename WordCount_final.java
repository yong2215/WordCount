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
	int allcnt; // jtable�� �ִ� �ܾ ������ �ܾ� ����
	HashMap<String, Integer> WordMap; // �����ڷ� �޾� ������ �ؽ���
	StringBuffer Ascii = new StringBuffer(); // String�� ���ڿ� ���ۿ� ȿ���� ������ Buffer�̿�
	String Ascii_new; // ��ȹ���ڸ� ���� ���ڿ�
	String[] ExceptWord = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "one", "after", "a", "i", "the", "s",
			"have", "including", "two", "under", "many", "such", "was", "who", "as", "an", "at", "be", "by", "which",
			"for", "he", "if", "in", "is", "it", "while", "that", "than", "of", "on", "or", "they", "so", "to", "un",
			"up", "this", "we", "has", "his", "been", "from", "according", "its", "those", "between", "only", "would",
			"well", "were", "added", "may", "about", "into", "because", "not", "when", "one", "out", "will", "with",
			"there", "their", "percent", "during", "other", "all", "and", "000", "are", "through", "both", "most",
			"also", "over", "but", "can", "said", "10", "20", "jae", "moon", "jong", "kim", "lee", "park", "talks",
			"more", "take", "some", "three", "high", "year", "make", "same", "top", "next", "won", "no", "u", "t",
			"had", "being", "30", "day", "month", "since", "years", "could", "made", "them", "like", "our" }; // ������ �ܾ���
																												// �迭

	Check(JTable table, JTextField txt1, JLabel label1, HashMap<String, Integer> WordMap, int allcnt) {
		this.table = table;
		this.txt1 = txt1;
		this.label1 = label1;
		this.WordMap = WordMap;
		this.allcnt = allcnt;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		HashMap<String, Integer> map2 = new HashMap<String, Integer>(); // ���ο� �ؽ����� �����Ѵ�.(������ ���ϱ����ؼ�)
		StringBuilder copy_builder = new StringBuilder(""); // ���ڿ� ���� ȿ���� ���ؼ� Builder�̿�
		String line = "";
		BufferedReader br = null;
		FileReader fr = null;

		for (String str : WordMap.keySet()) // ������ ���� �����Ͽ� ����Ѵ� (������ �ؽ��ʿ� ������ �����ʱ����ؼ�)
			map2.put(str, WordMap.get(str));
		try {
			terminate(); // ������ ������ �����ش�.
			fr = new FileReader(txt1.getText() + ".txt"); // ���� �̸��� �޴´�.
			br = new BufferedReader(fr); // ������ ���پ� �о���δ�.
			label1.setText(null); // �� �κ��� �����ش�.
			while ((line = br.readLine()) != null) // ���پ� �о�鿩�� ������ ���������� �ݺ�
			{
				copy_builder.append(line + ""); // ���� ������ copy��� ���ڿ��� ����
			}
			br.close();
			String copy = copy_builder.toString();
			putAscii(); // ������ ��ȹ���ڸ� ����.
			Ascii_new = Ascii.toString(); // buffer�� ������� ������ String�� Ascii_new�� ����
			getToken(copy, map2); // copy���� �ܾ �з��ؼ� map2�� ����
			Remover(map2); // map2���� ������ �ܾ ����
			Load(map2); // ��� ó���� ���� ������ ���̺� ���
		} catch (IOException ioe) {
			terminate(); // ������ ��µ� ������� �����.
			label1.setText("������ ���������ʽ��ϴ�"); // ������ ��� ���� ���� ��� "������ ���������ʽ��ϴ�"�� �󺧿� ����.
			return;
		}
	}

	void sort(String[][] array) // ����� data���� ������������ �����ϴ°�
	{
		String temp1, temp2, temp3, temp4;
		for (int i = 0; i < array[0].length; i++) {
			int max = i;
			for (int j = i + 1; j < array[0].length; j++) {
				if (Integer.parseInt(array[1][j]) > Integer.parseInt(array[1][max])) // string���� ����� ������ Integer�� �ٲ㼭 ��
					max = j;
			}
			temp1 = array[0][i]; // �ܾ� �̸� �κ�
			temp2 = array[0][max];
			temp3 = array[1][i]; // cnt �κ�
			temp4 = array[1][max];
			array[0][i] = temp2; // ���۷��� Ÿ���� ������ �����ʴ´� �׷��� ������Ƽ�긦 ��ȯ��Ų��.
			array[0][max] = temp1;
			array[1][i] = temp4;
			array[1][max] = temp3;
		}
	}

	void Remover(HashMap<String, Integer> map2) // �ܾ �����ϴ� �޼ҵ�
	{
		for (int i = 0; i < ExceptWord.length; i++)
			map2.remove(ExceptWord[i]);
	}

	void terminate() // ���̺� ��µ� ������ ����� �޼ҵ�
	{
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		if (allcnt != 0) // allcnt�� 0�� ��� �ݺ����� ���ư��� �����Ƿ� 0�� ���� ��Ų��. allcnt�� ���� ��� ������ ���� ������ ���ִ�
		{
			for (int i = 0; i < allcnt; i++)
				model.removeRow(0);
			allcnt = 0; // allcnt�� �ʱ�ȭ �����ش� (���� ������ ������ ��������)
		}
	}

	void Load(HashMap<String, Integer> map2) // ȭ�鿡 ���� �޼ҵ�
	{
		String a[] = new String[2];
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		String array[][] = new String[2][map2.size()]; // �ܾ��� ������ŭ �迭�� �����.
		int j = 0;
		for (String str : map2.keySet()) // �ؽ��ʿ� �ִ� ������ 2���� �迭�� �ű��.
		{
			array[0][j] = str;
			array[1][j] = map2.get(str) + "";
			j++;
		}
		sort(array); // 2���� �迭�� ����
		for (int i = 0; i < array[0].length; i++) // �迭�� ������ ���̺� ���´�.
		{
			a[0] = array[0][i];
			a[1] = array[1][i];
			allcnt++;
			model.addRow(a);
		}
	}

	void getToken(String copy, HashMap<String, Integer> map2) // �ܾ ������ �޼ҵ�
	{
		if (copy != null) // �о�� ������ ���� ���
		{
			StringTokenizer stok = new StringTokenizer(copy, Ascii_new); // putAscii�޼ҵ�� ���� ������� �������� �з�
			while (stok.hasMoreTokens()) // ������ ���������� �ݺ�
			{
				String word = stok.nextToken();
				word = word.toLowerCase(); // ���� ���ڰ� �ߺ� ����Ǵ� ���� ���ϱ� ���� �ҹ��ڷ� �� ����
				Integer cnt = map2.get(word); // �ؽ��ʿ� �ִ��� Ȯ��
				if (cnt == null) { // ������ ���� ����
					map2.put(word, 1);
				} else // ������ ������ cnt�� ����
					map2.put(word, ++cnt);
			}
		}
	}

	void putAscii() // ������ ��ȹ���ڸ� �����ϴ� �޼ҵ�
	{
		for (int i = 33; i <= 47; i++)
			Ascii.append((char) i);
		for (int i = 58; i <= 64; i++)
			Ascii.append((char) i);
		for (int i = 91; i <= 96; i++)
			Ascii.append((char) i);
		for (int i = 123; i < 127; i++)
			Ascii.append((char) i);
		Ascii.append(" ��������"); // "" '' �ʹ� ������ ���°� �ٸ� ���� ���� �߰�
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
