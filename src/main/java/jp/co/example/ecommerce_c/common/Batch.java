package jp.co.example.ecommerce_c.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.example.ecommerce_c.domain.Item;

public class Batch {
	public List<Item> importCSV() {

		FileInputStream fi = null;
		InputStreamReader is = null;
		BufferedReader br = null;

		List<Item> itemList = new ArrayList<Item>();

		try {
			// 読み込みファイルのインスタンス生成
			// ファイル名を指定する
			fi = new FileInputStream("src/main/resources/static/csv/data.csv");
			is = new InputStreamReader(fi);
			br = new BufferedReader(is);

			// 読み込み行
			String line;

			// 読み込み行数の管理
			int i = 0;

			// 列名を管理する為の配列
			String[] arr = null;

			// 1行ずつ読み込みを行う
			while((line = br.readLine()) != null) {
				Item item = new Item();
				// 先頭行は列名
				if(i == 0) {
					// カンマで分割した内容を配列に確認する
					arr = line.split(",");
				} else {
					// データ内容をコンソールに表示する
					System.out.println("---------------------------------");

					// データ件数を表示
					System.out.println("データ" + i + "件目");

					// カンマで分割した内容を配列に確認する
					String[] data = line.split(",");
					System.out.println("data=" + data);
					
					// 配列の中身を順位表示する。列名(=列名を格納した配列の要素数)分繰り返す
					int colno = 0;
					for(String colmn : arr) {
						System.out.println(colmn + ":" + data[colno]);
						colno++;
					}
					item.setName(data[0]);
					item.setDescription(data[1]);
					item.setPriceM(Integer.parseInt(data[2]));
					item.setPriceL(Integer.parseInt(data[3]));
					item.setImagePath(data[4]);
					item.setDeleted(Boolean.valueOf(data[5]));

					itemList.add(item);
				}
				// 行数のインクリメント
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemList;
	}

	public void exportCSV(List<Item> itemList) {
		Date d = new Date();
		SimpleDateFormat d1 = new SimpleDateFormat("yyyyMMdd");
		String s = d1.format(d);

		try {
			// 出力ファイルの作成
			FileWriter fw = new FileWriter("order_" + s + ".csv", false);

			// PrintWriterクラスのオブジェクトを生成
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

			// ヘッダーの指定
			pw.print("名前");
			pw.print(",");
			pw.print("説明");
			pw.print(",");
			pw.print("値段");
			pw.print(",");
			pw.print("色");
			pw.print(",");
			pw.print("削除判断");
			pw.println();

			// データを書き込む
			for(Item item : itemList) {
				pw.print(item.getName() + ", ");
				pw.print(item.getDescription() + ", ");
				pw.print(item.getPriceM() + ", ");
				pw.print(item.getPriceL() + ", ");
				pw.print(item.getImagePath() + ", ");
				pw.print(item.getDeleted());
				pw.println();
			}

			// ファイルを閉じる
			pw.close();

			// 出力確認用のメッセージ
			System.out.println("CSVファイルを出力しました");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
